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

import java.util.Locale;
import java.util.Scanner;

/**
 * Simple class for basic user input/output.
 */
class UserIO
{
	/**
	 * Prompt user for a single-character input.
	 *
	 * @param prompt
	 *     Prompt string
	 * @param ops
	 *     Options string containing permitted character responses (automatically converted
	 *     to upper case)
	 * @return
	 *     Character chosen by the user (automatically converted to upper case)
	 */
	static char userCharPrompt(
			final String prompt,
			final String ops)
	{
		/* case-insensitive comparison; convert everything to uppercase */
		final String options = ops.toUpperCase(Locale.ENGLISH);

		final Scanner kb = new Scanner(System.in);

		while (true)
		{
			RenameWand.stdout.print(prompt);
			RenameWand.stdout.flush();

			final String response = kb.nextLine().trim();

			if (response.length() != 1)
				continue;

			/* convert to char */
			final char c = response.toUpperCase(Locale.ENGLISH).charAt(0);

			if (options.indexOf(c) >= 0)
				return c;
		}
	}


	/**
	 * Print immediately to standard output.
	 *
	 * @param o
	 *     Object to be printed
	 */
	static void debug_p(
			final Object o)
	{
		RenameWand.stdout.print(o + "");
		RenameWand.stdout.flush();
	}
}
