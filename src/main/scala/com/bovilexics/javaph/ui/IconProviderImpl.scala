package com.bovilexics.javaph.ui
import javax.swing.ImageIcon

final class IconProviderImpl extends IconProvider
{
  override def getURL(location: String): String =
  {
    /*
		TODO fix this
		@NotNull final URL url = (new File(location)).toURI().toURL();
		return url;
		*/
    location
  }

  override def getImageIcon(location: String): ImageIcon = new ImageIcon(location)
}
