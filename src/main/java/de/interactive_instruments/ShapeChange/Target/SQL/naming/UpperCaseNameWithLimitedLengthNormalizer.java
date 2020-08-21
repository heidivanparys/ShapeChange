/**
 * ShapeChange - processing application schemas for geographic information
 *
 * This file is part of ShapeChange. ShapeChange takes a ISO 19109
 * Application Schema from a UML model and translates it into a
 * GML Application Schema or other implementation representations.
 *
 * Additional information about the software can be found at
 * http://shapechange.net/
 *
 * (c) 2002-2017 interactive instruments GmbH, Bonn, Germany
 *
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
 *
 * Contact:
 * interactive instruments GmbH
 * Trierer Strasse 70-72
 * 53115 Bonn
 * Germany
 */
package de.interactive_instruments.ShapeChange.Target.SQL.naming;

import org.apache.commons.lang3.StringUtils;

import de.interactive_instruments.ShapeChange.MessageSource;
import de.interactive_instruments.ShapeChange.ShapeChangeResult;

/**
 * @author Johannes Echterhoff (echterhoff at interactive-instruments dot de)
 *
 */
public class UpperCaseNameWithLimitedLengthNormalizer extends UpperCaseNameNormalizer implements MessageSource {

    protected ShapeChangeResult result;
    protected int maxLength;

    public UpperCaseNameWithLimitedLengthNormalizer(ShapeChangeResult result, int maxLength) {
	this.result = result;
	this.maxLength = maxLength;
    }

    @Override
    public String normalize(String stringToNormalize) {

	String normalizedName = StringUtils.substring(super.normalize(stringToNormalize), 0, maxLength);

	if (stringToNormalize.length() != normalizedName.length()) {
	    result.addDebug(this, 1, stringToNormalize, normalizedName);
	}

	return normalizedName;
    }

    @Override
    public String message(int mnr) {
	switch (mnr) {
	case 1:
	    return "Name '$1$' is truncated to '$2$'";
	default:
	    return "(" + UpperCaseNameWithLimitedLengthNormalizer.class.getName() + ") Unknown message with number: "
		    + mnr;
	}
    }
}
