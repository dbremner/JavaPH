package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.StatusLogger;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Line;
import com.bovilexics.javaph.qi.LineFactory;
import com.bovilexics.javaph.qi.QiAPI;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiProtocolException;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public final class ResultThreadCallable implements Callable<ResultThreadResult>
{
    @NonNls
    private static final @NotNull String DUPLICATE_FIELD_SUFFIX = ".1";
    // TODO Should these fields be volatile?

    private boolean error = false;

    private int entryIndex = 0;
    private int lastCode = QiAPI.LR_OK;
    private @NotNull ResultThreadState state = ResultThreadState.Starting;

    // Empty arrays are immutable according to Effective Java.
    private final @NotNull Object[][] emptyValues = new Object[0][0];

    private @NotNull ImmutableList<Object> headers = ImmutableList.of();
    private @NotNull Object[][] values = emptyValues;

    private final @NotNull Connection connection;
    private final @NotNull LineFactory lineFactory;
    private @Nullable Line qiLine;

    private final @NotNull String command;
    private final @NotNull String commandLine;
    private final @NotNull StatusLogger logger;
    private @NotNull String epilogue = "";
    private @NotNull String prologue = "";

    // Separator used in the display of queries which matched multiple records.
    private static final @NotNull String recordSeparator = "------------------------------------------------------------\n";

    private @NotNull StringBuilder rawResult = new StringBuilder();

    private final @NotNull List<List<Line>> records = new ArrayList<>();
    private @NotNull List<Line> record = new ArrayList<>();

    public ResultThreadCallable(final @NotNull StatusLogger logger, final @NotNull String command,
                                final @NotNull Connection connection) throws IOException
    {
        this.logger = logger;
        this.connection = connection;
        lineFactory = connection.getLineFactory();
        commandLine = command;
        this.command = new StringTokenizer(commandLine).nextToken();

        if (!this.connection.connected())
        {
            this.connection.connect();
        }
    }

    /**
     * This routine is called for each line that's read from the Qi server. Its
     * mission in life is to process each line, build the prologue, multiline
     * response, and epilogue. In addition it'll throw an exception if we find
     * ourselves in a weird state.
     *
     * It does however expect to be given a query response to build. It will get
     * all confused if given the response to a login request for example.
     *
     * @exception QiProtocolException in the event of Qi throwing us a curve ball.
     */
    private void add(final @NotNull String readFromServer, final @NotNull Line line) throws QiProtocolException
    {
        final int code = line.getCode();
        @NonNls final @NotNull String response = line.getResponse();
        switch (state)
        {
            case Starting:
                if (code >= QiAPI.LR_PROGRESS && code < QiAPI.LR_OK)
                {
                    // Some implementations of qi return a 102 response before the
                    // actual entries, giving the number of entries found; be prepared
                    // to see or not see this response.
                    prologue += response + "\n";
                    break;
                }
                else if (code == QiAPI.LR_OK || code == QiAPI.LR_RONLY)
                {
                    // Single line response.
                    prologue += response + "\n";
                    state = ResultThreadState.Ok;
                    lastCode = code;
                    break;
                }
                else if ((code < -QiAPI.LR_TEMP || code > QiAPI.LR_TEMP) && code != -QiAPI.LR_ABSENT)
                {
                    // This should be an error
                    prologue += response + "\n";
                    state = ResultThreadState.Error;
                    lastCode = code;
                    break;
                }
                else
                {
                    // This implementation of QI must not give LR_NUMRET.
                    state = ResultThreadState.InProgress;
                }

                // fall through

            case InProgress:
                if (code == -QiAPI.LR_OK
                        || code == -QiAPI.LR_RONLY
                        || code == -QiAPI.LR_AINFO
                        || code == -QiAPI.LR_ABSENT
                        || code == -QiAPI.LR_ISCRYPT)
                {
                    // Is this a new record?
                    final int index = line.getIndex();
                    if (index != entryIndex)
                    {
                        // It is a new record; append this record to the result and
                        // instantiate a new Vector object to hold the new record.
                        if (entryIndex != 0)
                        {
                            records.add(record);
                            record = new ArrayList<>();
                        }
                        entryIndex = index;
                    }
                    record.add(line);
                }
                else if (code >= QiAPI.LR_OK)
                {
                    // It must be done so finish it up.
                    epilogue += response + "\n";
                    records.add(record);
                    state = ResultThreadState.Ok;

                }
                else
                {
                    // If this ever happens then I've misinterpreted the protocol.
                    state = ResultThreadState.Unknown;
                    throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
                }
                break;

            case Unknown:
                throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
            case Error:
                if (code >= QiAPI.LR_TEMP)
                {
                    // End of error response.
                    prologue += response + "\n";

                    // Make a record of what the last code was.
                    lastCode = code;
                }
                else if (code < -QiAPI.LR_TEMP)
                {
                    // This should be a multiline error description.
                    prologue += response + "\n";
                }
                else
                {
                    state = ResultThreadState.Unknown;
                    throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
                }
                break;

            case Ok:
                // "200:Bye!" is all that should seen here (and that is disposed).
                if (code != QiAPI.LR_OK)
                {
                    state = ResultThreadState.Unknown;
                    throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
                }
                break;
            case Stopped:
            {
                //This shouldn't be possible.
                assert false;
            }
        }
    }

    private static @NotNull ImmutableList<Object> buildHeaders(final @NotNull List<List<Line>> list)
    {
        @NotNull String lastField = JavaPHConstants.UNKNOWN;
        final @NotNull List<Object> uniqueHeaders = new ArrayList<>();

        for (final @NotNull List<Line> currentQiLine : list)
        {
            for (final @NotNull Line line : currentQiLine)
            {
                @NotNull String field = line.getField();

                if (field.isEmpty())
                {
                    field = lastField + DUPLICATE_FIELD_SUFFIX;
                }

                if (!uniqueHeaders.contains(field))
                {
                    if (field.endsWith(DUPLICATE_FIELD_SUFFIX))
                    {
                        final int index = uniqueHeaders.indexOf(field.substring(0, field.lastIndexOf(DUPLICATE_FIELD_SUFFIX))) + 1;
                        uniqueHeaders.add(index, field);
                    }
                    else
                    {
                        uniqueHeaders.add(field);
                    }
                }

                lastField = field;
            }
        }

        return ImmutableList.copyOf(uniqueHeaders);
    }

    /**
     * This is the main routine that the Result thread executes.
     * When it exits, we should have a valid result.
     *
     * Quite simply, all that's done here is
     *  1. Connect to the server.
     *  2. Send the command that we were given.
     *  3. Read the response, line by line and building a complete result
     *     by calling the add() routine for each line.
     *  4. When we determine that we have a complete response, close the
     *     connection and signal that we're done.
     */
    private void buildResult() throws IOException, QiProtocolException
    {

        // Send query.
        connection.writeQI(commandLine + "\n");

        // Read the server's response, line by line.
        rawResult = new StringBuilder();
        int index = 0;
        @Nullable String readFromServer;
        while ((readFromServer = connection.readQI()) != null)
        {
            qiLine = lineFactory.create(readFromServer);

            add(readFromServer, qiLine);

            if (qiLine.getIndex() != index) {
                rawResult.append(recordSeparator);
            }

            index = qiLine.getIndex();
            rawResult.append(qiLine.getResponse().isEmpty() ? qiLine.getFieldValue() : qiLine.getResponse());
            rawResult.append("\n");

            // If code >= LR_OK, Qi has said all it's going to say.
            if (qiLine.getCode() >= QiAPI.LR_OK)
            {
                if (!connection.authenticated())
                {
                    connection.disconnect();
                }
                break;
            }

            if (qiLine != null) {
                lastCode = qiLine.getCode();
            }
        }

        if (qiLine != null) {
            lastCode = qiLine.getCode();
        }

        if (lastCode >= QiAPI.LR_TEMP)
        {
            state = ResultThreadState.Error;
            assert qiLine != null;
            prologue = qiLine.getResponse();

            final @NotNull String message = String.format(JavaPHConstants.GOT_ERROR_D_ON_LINE_S, qiLine.getCode(), readFromServer);

            logger.log(message);
        }
        else
        {
            state = ResultThreadState.Ok;
            if (command.equals(QiCommand.FIELDS))
            {
                headers = ResultThreadCallable.buildHeadersForFields();
                values = ResultThreadCallable.buildValuesForFields(headers, records);
            }
            else
            {
                headers = ResultThreadCallable.buildHeaders(records);
                values = buildValues(headers, records);
            }
        }
    }

    private @NotNull Object[][] buildValues(final @NotNull ImmutableList<Object> headersList,
                                            final @NotNull List<List<Line>> lists)
    {
        final Object[][] results = new Object[lists.size()][headersList.size()];

        int xCoordinate = -1;
        @NonNls @NotNull String lastField = JavaPHConstants.UNKNOWN;

        for (int i = 0; i < lists.size(); i++)
        {
            final int yCoordinate = i;
            final List<Line> list = lists.get(i);

            for (final @NotNull Line line : list)
            {
                @NotNull String field = line.getField();

                if (field.isEmpty())
                {
                    field = lastField + DUPLICATE_FIELD_SUFFIX;
                }

                boolean found = false;
                for (int k = 0; k < headersList.size() && !found; k++)
                {
                    final @NotNull Object value = headersList.get(k);
                    if (value.equals(field))
                    {
                        xCoordinate = k;
                        found = true;
                    }
                }

                if (found)
                {
                    results[yCoordinate][xCoordinate] = line.getValue();
                }
                else
                {
                    final @NotNull String message = String.format(JavaPHConstants.COULDN_T_FIND_HEADER_FOR_THIS_COLUMN_S, line.toString());

                    logger.log(message);
                }

                lastField = field;
            }
        }
        return results;
    }

    private static @NotNull ImmutableList<Object> buildHeadersForFields()
    {
        return ImmutableList.copyOf(new Object[] {JavaPHConstants.NAME, JavaPHConstants.DESCRIPTION,
                JavaPHConstants.PROPERTIES});
    }

    private static @NotNull Object[][] buildValuesForFields(final @NotNull ImmutableList<Object> headersList,
                                                            final @NotNull List<List<Line>> lists)
    {
        final @NotNull Object[][] results = new Object[lists.size()][headersList.size()];

        for (int i = 0; i < lists.size(); i++)
        {
            final List<Line> list = lists.get(i);

            assert list.size() % 2 == 0;
            for (int j = 0; j < list.size() - 1; j = j + 2)
            {
                final Line propsQiLine = list.get(j);
                final Line descQiLine = list.get(j + 1);

                final @NotNull String field = propsQiLine.getField();
                final @NotNull String props = propsQiLine.getValue();
                final @NotNull String desc = descQiLine.getValue();

                results[i][0] = field;
                results[i][1] = desc;
                results[i][2] = props;
            }
        }
        return results;
    }

    private void cleanup() throws IOException, QiProtocolException
    {
        state = ResultThreadState.Stopped;
        prologue = JavaPHConstants.STOPPED;

        // Read the remainder of the response from Qi (and dispose).
        if (qiLine != null && qiLine.getCode() < QiAPI.LR_OK)
        {
            @Nullable String buffer;
            while ((buffer = connection.readQI()) != null)
            {
                final @NotNull Line line = lineFactory.create(buffer);
                if (line.getCode() >= QiAPI.LR_OK) {
                    break;
                }
            }
        }

        if (!connection.authenticated()) {
            connection.disconnect();
        }
    }

    @Override
    public @NotNull ResultThreadResult call() throws Exception
    {
        // TODO dead branch?
        if (error)
        {
            state = ResultThreadState.Error;
            logger.showStatus(JavaPHConstants.ERROR_INVALID_CONNECTION_QUERY_STOPPED);
            throw new InvalidStateException(JavaPHConstants.ERROR_INVALID_CONNECTION_QUERY_STOPPED);
        }

        connection.lock();

        try
        {
            buildResult();

            if (!connection.authenticated()) {
                connection.disconnect();
            }
        }
        catch (@NotNull QiProtocolException | IOException e)
        {
            error = true;
            logger.showStatus(String.format(JavaPHConstants.ERROR_S, e));
        } finally
        {
            connection.unlock();
        }
        assert state == ResultThreadState.Ok;
        final @NotNull String rawResultLocal = rawResult.toString();
        final @NotNull ImmutableList<Object> headersLocal = headers;
        final @NotNull Object[][] valuesLocal = values.clone();

        final ResultThreadResult result = new ResultThreadResult(rawResultLocal, headersLocal, valuesLocal);
        return result;
    }
}
