/*
 * Copyright (C) 2003  Robert Fernandes  robert@bovilexics.com
 * http://www.bovilexics.com/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */
package com.bovilexics.javaph.qi;

/*
	From the "protocol" document:

	The major properties fields may have are Indexed, Public,
	Default, Lookup, and Change.  Fields marked Indexed are kept
	track of in the database's index.  At least one such field must
	be included in every query.  Fields marked Public may be viewed
	by anyone using qi in anonymous or login mode.  Fields not marked
	Public may only be viewed by the entry's owner in login mode, or
	by someone using qi in hero mode.  Default fields are printed if
	no "return" clause is given in a query.  Lookup fields may be
	used in the selection part of a query; a field not marked Lookup
	cannot be used to select entries.[13] Finally, a user in login
	mode in qi may change any of his or her fields that are marked
	Change.


	From the "install" document:

	The attributes and their functions are:

	I    Indexed; the contents of this field will be put in the data-
	     base index.  At least one Indexed field must be included in
	     every query.

	L    Lookup; users may use this field in queries.

	P    Public; the contents of this field may be displayed to any-
	     one (but see "F" below).

	D    Default; this field will be returned for queries that do not
	     specify which fields to return.

	C    Change; users may change the contents of this field.

	F    ForcePub; users may not suppress this field.  Fields not
	     marked "F" may be hidden from view by putting something
	    (anything) in the F_SUPPRESS field.

	N    NoPeople; users may change this field, but only if their
	     F_TYPE field does not contain "person".

	E    Encrypt; this field may not cross the network, nohow, noway.

	W    Any (Wild); fields so marked may be searched collectively by
	     specifying an "any" field in a query.

	There are other defined attributes, but they are not used at this
	time.
 */

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * 
 * Qi field properties
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
enum QiFieldProperties
{
    ;
    @NonNls
    public static final @NotNull String INDEXED	= "Indexed";
	@NonNls
	public static final @NotNull String LOOKUP	= "Lookup";
	@NonNls
	public static final @NotNull String PUBLIC	= "Public";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	@NonNls
	public static final @NotNull String DEFAULT	= "Default";
	@NonNls
	public static final @NotNull String CHANGE	= "Change";
	@NonNls
	public static final @NotNull String FORCEPUB	= "ForcePub";
	@NonNls
	public static final @NotNull String NOPEOPLE	= "NoPeople";
	@NonNls
	public static final @NotNull String ENCRYPT	= "Encrypt";
	@NonNls
	public static final @NotNull String ANY		= "Any";
}
