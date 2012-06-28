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
 * Represent a double value used in enumeration of files/directories.
 */
class DoubleEnumerationUnit
		implements Comparable<DoubleEnumerationUnit>
{
	/** index of the corresponding file/directory */
	int index;

	/** double value to be used in sorting */
	double value;


	/**
	 * Constructor.
	 *
	 * @param index
	 *     Index of the corresponding file/directory
	 * @param val
	 *     Double value to be used in sorting.
	 */
	DoubleEnumerationUnit(
			int index,
			double value)
	{
		this.index = index;
		this.value = value;
	}


	/** compare this object to the specified object */
	@Override
	public int compareTo(
			DoubleEnumerationUnit o)
	{
		if (this.value > o.value)
		{
			return 1;
		}
		else if (this.value < o.value)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}


	/** indicate if this object is equal to the specified object */
	@Override
	public boolean equals(
			Object o)
	{
		if (o instanceof DoubleEnumerationUnit)
			return (this.value == ((DoubleEnumerationUnit) o).value);

		return false;
	}


	/** return a hash code value for this object */
	@Override
	public int hashCode()
	{
		return (int) this.value;
	}
}
