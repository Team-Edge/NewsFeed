package database;


public class DBTester {

	public DBTester() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DBconnection con = new DBconnection("jdbc:mysql://127.0.0.1:3306/newsfeed", "root", "");
			QueryExample qe = new QueryExample(con);
			qe.query();
			qe.doSomethingWithResults();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
