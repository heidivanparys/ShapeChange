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
 * (c) 2002-2016 interactive instruments GmbH, Bonn, Germany
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
package de.interactive_instruments.ShapeChange.Target.SQL;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.interactive_instruments.ShapeChange.MapEntryParamInfos;
import de.interactive_instruments.ShapeChange.MessageSource;
import de.interactive_instruments.ShapeChange.ProcessMapEntry;
import de.interactive_instruments.ShapeChange.ShapeChangeResult;

/**
 * @author Johannes Echterhoff (echterhoff <at> interactive-instruments
 *         <dot> de)
 *
 */
public class SQLServerStrategy implements DatabaseStrategy, MessageSource {

	public static final String IDX_PARAM_USING = "USING";
	public static final String IDX_PARAM_BOUNDING_BOX = "BOUNDING_BOX";

	private ShapeChangeResult result;

	public SQLServerStrategy(ShapeChangeResult result) {
		this.result = result;
	}

	@Override
	public String convertDefaultValue(boolean b) {
		if (b)
			return "'true'";
		else
			return "'false'";
	}

	@Override
	public String primaryKeyDataType() {
		return "int";
	}

	@Override
	public String geometryDataType(ProcessMapEntry me, int srid) {
		// srid is ignored here
		return me.getTargetType();
	}

	@Override
	public String unlimitedLengthCharacterDataType() {
		return "nvarchar(max)";
	}

	@Override
	public String limitedLengthCharacterDataType(int size) {
		return "nvarchar(" + size + ")";
	}

	@Override
	public String geometryIndexColumnPart(String indexName, String tableName,
			String columnName, Map<String, String> geometryCharacteristics) {

		// TBD: declaration of tesselation
		String using = null;
		if (geometryCharacteristics.containsKey(IDX_PARAM_USING)) {
			using = geometryCharacteristics.get(IDX_PARAM_USING);
		}
		using = (using == null ? "" : "USING " + using + " ");

		String boundingBox = "BOUNDING_BOX = (-180,-90,180,90)";
		if (geometryCharacteristics.containsKey(IDX_PARAM_BOUNDING_BOX)) {
			boundingBox = "BOUNDING_BOX = "
					+ geometryCharacteristics.get(IDX_PARAM_BOUNDING_BOX);
		}

		return "CREATE SPATIAL INDEX " + indexName + " ON " + tableName + "("
				+ columnName + ") " + using + "WITH (" + boundingBox + ")";
	}

	@Override
	public String geometryMetadataUpdateStatement(String normalizedClassName,
			String columnname, int srid) {

		// TBD: should we constrain the SRID as follows?
		// ALTER TABLE xyz ADD CONSTRAINT cname CHECK (column.STSrid = srid)
		return "";
	}

	@Override
	public String normalizeName(String name) {

		String normalizedName = StringUtils.substring(name, 0, 128);
		if (name.length() != normalizedName.length()) {
			result.addWarning(this, 1, name, normalizedName);
		}
		return normalizedName;
	}

	@Override
	public boolean validate(Map<String, ProcessMapEntry> mapEntryByType,
			MapEntryParamInfos mepp) {
		
		// TODO implement specific checks
		
		// BOUNDING_BOX must contain four numbers, etc.
		return true;
	}

	@Override
	public String createNameCheckConstraint(String tableName,
			String propertyName) {

		String truncatedName = StringUtils
				.substring(tableName + "_" + propertyName, 0, 125);
		String checkConstraintName = truncatedName + "_CK";
		return checkConstraintName;
	}

	@Override
	public String message(int mnr) {
		switch (mnr) {
		case 0:
			return "Context: class SQLServerStrategy";
		case 1:
			return "Name '$1$' is truncated to '$2$'";
		default:
			return "(Unknown message)";
		}
	}
}