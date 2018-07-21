<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.*" %>
<%!
	private static final class DataService {

		private Connection _cn = null;
		private PreparedStatement _st = null;
		private ResultSet _rs = null;

		public DataService() {

		}

		private final Connection open() throws Exception {

			if(this._cn != null)
				return this._cn;
			Class.forName("oracle.jdbc.driver.OracleDriver");
			final String source = "jdbc:oracle:thin:@999.999.999.999:1521:SID";
			this._cn = DriverManager.getConnection(source, "USER", "PASSWORD");
			return this._cn;
		}

		public final ResultSet query(String sql, Object ... params) throws Exception {

			if(this._rs != null) {
				this._rs.close();
				this._rs = null;
			}
			if(this._st != null) {
				this._st.close();
				this._st = null;
			}

			this._st = this.open().prepareStatement(sql);
			int index = 0;
			for(Object x : params) {
				this._st.setObject(1 + index, x);
				index++;
			}
			this._rs = this._st.executeQuery();
			return this._rs;
		}

		public final void close() throws Exception {

			if(this._rs != null) {
				this._rs.close();
				this._rs = null;
			}

			if(this._st != null) {
				this._st.close();
				this._st = null;
			}

			if(this._cn != null) {
				this._cn.close();
				this._cn = null;
			}
		}
	}

	private static final String toString(Object x) {

		if(x == null)
			return "";
		if(x instanceof java.sql.Timestamp) {
			final java.sql.Timestamp time = (java.sql.Timestamp)x;
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time);
		}
		return x.toString();
	}

	private static final String urlencode(Object x) {

		if(x == null)
			return "";
		return java.net.URLEncoder.encode(x.toString());
	}

	private static final Object getTimestamp() {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(
			Calendar.getInstance().getTime());
	}

	private static final String htmlencode(Object x) {

		if(x == null)
			return "";
		String s = x.toString();
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\"", "&quot;");
		return s;
	}

	private static void showTables(PageContext context) throws Exception {

		final DataService service = new DataService();

		try {
	
			final JspWriter out = context.getOut();

			// ================================================================
			// 情報欄
			// ================================================================
			{
				out.println("オブジェクトの一覧を表示しています<br>");
			}

			// ================================================================
			// オブジェクトの一覧
			// ================================================================
			{
				final ResultSet rs = service.query(
					"select * from USER_TABLES order by 1, 2");
				final ResultSetMetaData m = rs.getMetaData();
				final int column_count = m.getColumnCount();
				out.println("<table class=\"main\">");
				out.println("<tr>");
				for(int i = 0; i < column_count; i++) {
					final String column_name = m.getColumnName(1 + i);
					out.print("<td class=\"header-cell\">");
					out.print(htmlencode(column_name));
					out.println("</td>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for(int i = 0; i < column_count; i++) {
						final String column_name = m.getColumnName(1 + i);
						out.print("<td class=\"normal-cell\" nowrap>");
						if("TABLE_NAME".equals(column_name)) {
							final Object table_schema = "dummy"; //rs.getObject("OWNER");
							final Object table_name = rs.getObject(1 + i);
							out.print("<a href=\"isqlplus.jsp?request=desc&schema=");
							out.print(urlencode(table_schema));
							out.print("&object=");
							out.print(urlencode(table_name));
							out.print("\">");
							out.print(htmlencode(toString(table_name)));
							out.print("</a>");
						}
						else {
							out.print(htmlencode(toString(rs.getObject(1 + i))));
						}
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}
		}
		finally {

			service.close();
		}
	}

	private static void describeObject(PageContext context) throws Exception {

		final DataService service = new DataService();

		try {

			final ServletRequest request = context.getRequest();
			final JspWriter out = context.getOut();

			final String table_schema = request.getParameter("schema");
			final String object_name = request.getParameter("object");

			// ================================================================
			// 情報欄
			// ================================================================
			{
				System.out.println("$$$ 情報を表示 $$$");
				out.print("オブジェクト [");
				out.print(htmlencode(table_schema));
				out.print(".");
				out.print(htmlencode(object_name));
				out.print("] の詳細を表示しています (");
				out.print("<a href=\"isqlplus.jsp?request=rows&schema=");
				out.print(urlencode(table_schema));
				out.print("&object=");
				out.print(urlencode(object_name));
				out.print("\">レコードを表示する</a>)");
				out.println("<br>");
			}

			// ================================================================
			// 詳細を表示
			// ================================================================
			{
				System.out.println("$$$ 詳細を表示 $$$");
				final ResultSet rs = service.query(
					"select * from USER_TAB_COLUMNS" +
					" where TABLE_NAME = ? order by 1, 2",
					object_name);
				final ResultSetMetaData m = rs.getMetaData();
				final int column_count = m.getColumnCount();
				out.println("<table class=\"main\">");
				out.println("<tr>");
				for(int i = 0; i < column_count; i++) {
					final String column_name = m.getColumnName(1 + i);
					if("TABLE_NAME".equals(column_name))
						continue;
					out.print("<td class=\"header-cell\">");
					out.print(column_name);
					out.println("</td>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for(int i = 0; i < column_count; i++) {
						final String column_name = m.getColumnName(1 + i);
						if("TABLE_NAME".equals(column_name))
							continue;
						out.print("<td class=\"normal-cell\" nowrap>");
						if("TABLE_NAME".equals(object_name)) {
						}
						else {
							out.print(htmlencode(toString(rs.getObject(1 + i))));
						}
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}

			// ================================================================
			// インデックス情報
			// ================================================================
			{
				System.out.println("$$$ インデックス情報を表示 $$$");
				final ResultSet rs = service.query(
					"select *" +
					" from USER_IND_COLUMNS" +
					" where TABLE_NAME = ?" +
					" order by INDEX_NAME, COLUMN_POSITION",
					object_name);
				final ResultSetMetaData m = rs.getMetaData();
				final int column_count = m.getColumnCount();
				out.println("<br>");
				out.println("CONSTRAINTS");
				out.println("<table class=\"main\">");
				out.println("<tr>");
				for(int i = 0; i < column_count; i++) {
					final String column_name = m.getColumnName(1 + i);
					out.print("<td class=\"header-cell\">");
					out.print(column_name);
					out.println("</td>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for(int i = 0; i < column_count; i++) {
						out.print("<td class=\"normal-cell\" nowrap>");
						out.print(htmlencode(toString(rs.getObject(1 + i))));
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}

			System.out.println("$$$ exit $$$");
		}
		finally {

			service.close();
		}
	}

	private static void printRows(PageContext context) throws Exception {

		final DataService service = new DataService();

		try {

			final ServletRequest request = context.getRequest();
			final JspWriter out = context.getOut();
			final String table_schema = request.getParameter("schema");
			final String object_name = request.getParameter("object");

			// ================================================================
			// 情報欄
			// ================================================================
			{
				out.print("オブジェクト [");
				//out.print(htmlencode(table_schema));
				//out.print(".");
				out.print(htmlencode(object_name));
				out.print("] のレコードを表示しています (");
				out.print("<a href=\"isqlplus.jsp?request=desc&schema=");
				out.print(urlencode(table_schema));
				out.print("&object=");
				out.print(urlencode(object_name));
				out.print("\">詳細を表示する</a>)");
				out.println("<br>");
			}

			// ================================================================
			// すべてのレコードを表示
			// ================================================================
			{
				final ResultSet rs = service.query(
					"select * from " + object_name);
				final ResultSetMetaData m = rs.getMetaData();
				final int column_count = m.getColumnCount();
				out.println("<table class=\"main\">");
				out.println("<tr>");
				for(int i = 0; i < column_count; i++) {
					final String column_name = m.getColumnName(1 + i);
					out.print("<td class=\"header-cell\" nowrap>");
					out.print(htmlencode(column_name));
					out.println("</td>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for(int i = 0; i < column_count; i++) {
						Object unknown = rs.getObject(1 + i);
						out.print("<td class=\"normal-cell\"");
						if(unknown instanceof java.sql.Timestamp)
							out.print(" nowrap");
						out.print(">");
						out.print(htmlencode(toString(unknown)));
						out.print("<!--");
						out.print(htmlencode(unknown == null ? "" : unknown.getClass()));
						out.print("-->");
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}
		}
		finally {

			service.close();
		}
	}

	private static void createMenuItems(PageContext context) throws Exception {

		final JspWriter out = context.getOut();
		out.println("<a href=\"isqlplus.jsp\">オブジェクト一覧</a>");
		out.println("<a href=\"isqlplus.jsp?request=query\">クエリ</a>");
	}

	private static final int length(String x) {

		if(x == null)
			return 0;
		return x.length();
	}

	private static void executeQuery(PageContext context) throws Exception {

		final DataService service = new DataService();

		try {

			final ServletRequest request = context.getRequest();
			final JspWriter out = context.getOut();

			final String sql = request.getParameter("statement");

			// ================================================================
			// 情報欄
			// ================================================================
			{
			}

			// ================================================================
			// 入力欄
			// ================================================================
			{
				out.print("<textarea name=\"statement\" style=\"width: 700px; height: 270px;\">");
				out.print(htmlencode(sql));
				out.print("</textarea><br>");
				out.print("<input type=\"hidden\" name=\"request\" value=\"query\">");
				out.print("<input type=\"submit\" style=\"width: 200px\">");
				out.println("<hr>");
			}

			if(length(sql) == 0) 
				return;

			// ================================================================
			// クエリを実行(参照系のみ)
			// ================================================================
			{
				final ResultSet rs = service.query(sql);
				final ResultSetMetaData m = rs.getMetaData();
				final int column_count = m.getColumnCount();
				out.println("<table class=\"main\">");
				out.println("<tr>");
				for(int i = 0; i < column_count; i++) {
					final String column_name = m.getColumnName(1 + i);
					out.print("<td class=\"header-cell\">");
					out.print(htmlencode(column_name));
					out.println("</td>");
				}
				out.println("</tr>");
				while(rs.next()) {
					out.println("<tr>");
					for(int i = 0; i < column_count; i++) {
						final String column_class_name = m.getColumnClassName(1 + i);
						out.print("<td class=\"normal-cell\"");
						if("java.sql.Timestamp".equals(column_class_name))
							out.print(" nowrap");
						out.print(">");
						out.print(htmlencode(toString(rs.getObject(1 + i))));
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}
		}
		finally {

			service.close();
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>isqlplus</title>
<style>
table.main {
	border: solid 0px #222222;
	border-collapse: collapse;
	/*border-spacing: 3px;*/
	/*border-width: 1px;*/
	/*border-left: 1;*/
}

td.header-cell {
	border: solid 1px #000000;
	align: left;
	vertical-align: top;
	font-family: "ＭＳ ゴシック";
	font-size: 9pt;
}

td.normal-cell {
	border: solid 1px #000000;
	align: left;
	vertical-align: top;
	font-family: "ＭＳ ゴシック";
	font-size: 9pt;
}
</style>
</head>
<body>
	<form method="post" action="isqlplus.jsp">
<%
	try {

		// ====================================================================
		// メニュー
		// ====================================================================
		createMenuItems(pageContext);
		out.println("<hr>");

		// ====================================================================
		// コンテンツ
		// ====================================================================
		final String requested_type = request.getParameter("request");
		if("desc".equals(requested_type)) {
			// テーブルの詳細を表示
			describeObject(pageContext);
		}
		else if("query".equals(requested_type)) {
			// テーブルの一覧を表示
			executeQuery(pageContext);
		}
		else if("rows".equals(requested_type)) {
			// オブジェクトのレコードを表示
			printRows(pageContext);
		}
		else {
			// テーブル一覧の表示
			showTables(pageContext);
		}
	}
	catch(Exception e) {

		out.println("<div style=\"color: #ff0000\">RUNTIME EXCEPTION! <br>");
		out.println(htmlencode(e));
		out.println("</div>");
		e.printStackTrace();
	}
	finally {

	}
%>
	</form>
</body>
<!-- <%=htmlencode(getTimestamp()) %> -->
</html>
