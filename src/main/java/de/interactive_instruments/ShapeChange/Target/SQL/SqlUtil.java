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
package de.interactive_instruments.ShapeChange.Target.SQL;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.interactive_instruments.ShapeChange.Model.Info;
import de.interactive_instruments.ShapeChange.Model.PropertyInfo;
import de.interactive_instruments.ShapeChange.Target.SQL.expressions.Expression;
import de.interactive_instruments.ShapeChange.Target.SQL.expressions.ExpressionList;
import de.interactive_instruments.ShapeChange.Target.SQL.expressions.StringValueExpression;
import de.interactive_instruments.ShapeChange.Target.SQL.structure.Column;
import de.interactive_instruments.ShapeChange.Target.SQL.structure.Table;

/**
 * @author Johannes Echterhoff (echterhoff <at> interactive-instruments
 *         <dot> de)
 *
 */
public class SqlUtil {

	public static List<StringValueExpression> toStringValueList(
			String... strings) {

		List<StringValueExpression> result = new ArrayList<StringValueExpression>();

		for (String string : strings) {
			result.add(new StringValueExpression(string));
		}

		return result;
	}

	public static List<Column> toColumnList(Table inTable,
			String... columnNames) {

		List<Column> result = new ArrayList<Column>();

		for (String name : columnNames) {
			result.add(new Column(name, null, inTable));
		}

		return result;
	}

	public static ExpressionList toExpressionList(
			List<? extends Expression> list) {

		ExpressionList result = new ExpressionList();
		List<Expression> tmp = new ArrayList<Expression>();
		result.setExpressions(tmp);

		for (Expression exp : list) {
			tmp.add(exp);
		}

		return result;
	}

	/**
	 * Prints the list, using the toString() method of its members.
	 *
	 * @param list
	 *            list of objects to print
	 * @param useComma
	 *            true if the printed members shall be separated by a comma, in
	 *            addition to a space character
	 * @param useBrackets
	 *            true if the list shall be enclosed in brackets
	 * @return String representation of the list
	 */
	public static String getStringList(List<?> list, boolean useComma,
			boolean useBrackets) {

		StringBuilder sb = new StringBuilder();

		if (list != null && !list.isEmpty()) {

			String separator = ", ";
			if (!useComma) {
				separator = " ";
			}

			if (useBrackets) {
				sb.append("(");
			}

			sb.append(StringUtils.join(list, separator));

			if (useBrackets) {
				sb.append(")");
			}
		}

		return sb.toString();
	}

	/**
	 * Determines the name of the given table, for subsequent use by the target.
	 * By default, the table name is returned. However, if parameter
	 * useShortName is true, the info object represented by the table (a class,
	 * an association, or a property) has a tagged value with the short name,
	 * and that tagged value is not blank, then the short name is returned.
	 * 
	 * @param col
	 * @param useShortName
	 *            <code>true</code> if the short name should be used, if
	 *            available
	 * @return
	 */
	public static String determineName(Table table, boolean useShortName) {

		String result = table.getName();

		if (useShortName) {

			Info representedInfo = table.getRepresentedClass();

			if (representedInfo == null) {
				representedInfo = table.getRepresentedAssociation();
			}

			if (representedInfo == null) {
				representedInfo = table.getRepresentedProperty();
			}

			if (representedInfo != null
					&& StringUtils.isNotBlank(representedInfo
							.taggedValue(SqlDdl.shortNameByTaggedValue))) {

				result = representedInfo
						.taggedValue(SqlDdl.shortNameByTaggedValue).trim();
			}
		}

		return result;
	}

	/**
	 * Determines the name of the given column, for subsequent use by the
	 * target. By default, the column name is returned. However, if parameter
	 * useShortName is true, the property represented by the column has a tagged
	 * value with the short name, and that tagged value is not blank, then the
	 * short name is returned.
	 * 
	 * @param col
	 * @param useShortName
	 *            <code>true</code> if the short name should be used, if
	 *            available
	 * @return
	 */
	public static String determineName(Column col, boolean useShortName) {

		String result = col.getName();

		if (useShortName) {

			Info representedInfo = col.getRepresentedProperty();

			if (representedInfo != null
					&& StringUtils.isNotBlank(representedInfo
							.taggedValue(SqlDdl.shortNameByTaggedValue))) {

				result = representedInfo
						.taggedValue(SqlDdl.shortNameByTaggedValue).trim();
			}
		}

		return result;
	}

	public static String determineName(PropertyInfo pi, boolean useShortName) {

		String result = pi.name();

		if (useShortName) {

			if (StringUtils.isNotBlank(
					pi.taggedValue(SqlDdl.shortNameByTaggedValue))) {

				result = pi.taggedValue(SqlDdl.shortNameByTaggedValue).trim();
			}
		}

		return result;
	}
}
