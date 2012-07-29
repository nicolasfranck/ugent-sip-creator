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

package RenameWandLib;

import java.io.File;


/**
 * Represent a source-target pair for renaming a file/directory.
 */
public class RenameFilePair
{
	/** source file/directory */
	File source;

	/** target file/directory */
	File target;

	/** true if rename operation is successful; false otherwise */
	boolean success;


	/**
	 * Constructor.
	 *
	 * @param source
	 *     Source file/directory.
	 * @param target
	 *     Target file/directory.
	 */
	RenameFilePair(
			File source,
			File target)
	{
		this.source = source;
		this.target = target;
	}

        public File getSource() {
            return source;
        }

        public boolean isSuccess() {
            return success;
        }

        public File getTarget() {
            return target;
        }

}
