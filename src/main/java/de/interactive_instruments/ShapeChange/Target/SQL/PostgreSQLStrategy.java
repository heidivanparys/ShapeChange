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
 * (c) 2002-2015 interactive instruments GmbH, Bonn, Germany
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

import java.util.Locale;
import java.util.Map;

import de.interactive_instruments.ShapeChange.MapEntryParamInfos;
import de.interactive_instruments.ShapeChange.ProcessMapEntry;

public class PostgreSQLStrategy implements DatabaseStrategy {

	@Override
	public String primaryKeyDataType() {
		return "bigserial";
	}
	
	@Override
	public String convertDefaultValue(boolean b) {
		if(b)
			return "true";
		else
			return "false";
	}

	@Override
	public String geometryDataType(ProcessMapEntry me, int srid) {
		return "geometry(" + me.getTargetType() + "," + srid + ")";
	}

	@Override
	public String unlimitedLengthCharacterDataType() {
		return "text";
	}

	@Override
	public String limitedLengthCharacterDataType(int size) {
		return "character varying(" + size + ")";
	}

	@Override
	public String geometryIndexColumnPart(String indexName, String tableName,
			String columnName, Map<String, String> geometryCharacteristics) {
		return "CREATE INDEX " + indexName + " ON " + tableName
				+ " USING GIST (" + columnName + ")";
	}

	@Override
	public String geometryMetadataUpdateStatement(String normalizedClassName,
			String columnname, int srid) {
		// in PostGIS 2.0, geometry_column is a view
		return "";
	}

	@Override
	public String normalizeName(String name) {
		return name.toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String createNameCheckConstraint(String tableName,
			String propertyName) {
		return tableName.toLowerCase(Locale.ENGLISH) + "_"
				+ propertyName.toLowerCase(Locale.ENGLISH) + "_chk";
	}

	@Override
	public boolean validate(Map<String, ProcessMapEntry> mapEntryByType,
			MapEntryParamInfos mepp) {
		// nothing specific to check
		return true;
	}

}
