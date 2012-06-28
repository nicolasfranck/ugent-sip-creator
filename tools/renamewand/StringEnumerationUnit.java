/**
 * RenameWand 2.2
 * Copyright 2007 Zach Scrivena
 * 2007-12-09
 * zachscrivena@gmail.com
 * http://renamewand.sourceforge.net/
 *
 * RenameWand is a simple command-line utility for renaming files or
 * directories using an intuitive but powerful syntax.
 *
 * TERMS AND CONDITIONS:
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package renamewand;


/**
 * Represent a string used in enumeration of files/directories.
 */
class StringEnumerationUnit
		implements Comparable<StringEnumerationUnit>
{
	/** index of the corresponding file/directory */
	int index;

	/** String to be used in sorting */
	String value;


	/**
	 * Constructor.
	 *
	 * @param index
	 *     Index of the corresponding file/directory
	 * @param val
	 *     String to be used in sorting.
	 */
	StringEnumerationUnit(
			int index,
			String value)
	{
		this.index = index;
		this.value = value;
	}


	/** compare this object to the specified object */
	@Override
	public int compareTo(
			final StringEnumerationUnit o)
	{
		return this.value.compareTo(o.value);
	}


	/** indicate if this object is equal to the specified object */
	@Override
	public boolean equals(
			final Object o)
	{
		if (o instanceof StringEnumerationUnit)
			return this.value.equals(((StringEnumerationUnit) o).value);

		return false;
	}


	/** return a hash code value for this object */
	@Override
	public int hashCode()
	{
		return this.value.hashCode();
	}
}
