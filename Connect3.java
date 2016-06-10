package connect;

//import java.util.List;
//import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import display.Display;

/*
@author testakaunto 2016/6/3
@editor testakaunto 2016/6/5
@editor testakaunto 2016/6/7
*/
//INSERT INTO soft2 VALUES( \"A0021\" , \"剣の伝説2\" , \"ツルギノデンセツツー\" , 5000 , \"RPG\" , \"2016-2-8\" , \"Tuu\" , \"新品\" , 20 )";
//DELETE FROM soft2 WHERE ID = \"A0021\"
public class Connect3{

	private final String SUPERSQL = "SELECT * FROM soft2";
	//*******更新系処理を行うときはテーブル名変更に注意***********
	private String sql = "DELETE FROM soft2 WHERE ID = \"A0021\"";
	private Connection connection;
	private ResultSet resultset;
	private PreparedStatement preparedstatement;
	private ResultSetMetaData resultsetmetadata;
	private Display display;
	
	//コンストラクタ群
	public Connect3() {
		display = new Display();
		try{
			Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			//**getConnectionの引数2,3のユーザー名、パスワードは各自の環境に合わせてください**
			connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/item" , "root" , "" );
			System.out.println( "MySQLに接続できました." );
		}catch( InstantiationException e ){
			System.out.println( "JDBCドライバのロードに失敗しました.　原因：InstantiationException" );
		}catch( IllegalAccessException e ){
			System.out.println( "JDBCドライバのロードに失敗しました.　原因：IllegalAccessException" );
		}catch( ClassNotFoundException e ){
			System.out.println( "JDBCドライバのロードに失敗しました.　原因：ClassNotFoundException" );
		}catch( SQLException e ){
			System.out.println( "MySQLに接続できませんでした." );
		}
	}
	//インスタンス生成時にSQL文をセットする場合こちらのコンストラクタを使用
	public Connect3( String sql ){
		this.sql = sql;
	}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/5
	データベースと接続するためのメソッド
	SQL文の内容によって処理を分岐する。
	処理内容はDisplay型のメソッドを利用する。
	------------------------------------------------------------------------
	*/
	public void connectDB(){
		try{
			//JDBCドライバをロードします
			//Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			
			
			//preparedstatementを使います
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery();
			
			//SQL文がSELECT文かどうか判断
			if( sql.matches( "SELECT.*" ) ){
				//SQL文中のアスタリスクの検索。なければ、各カラム名を書いていると判断
				if( sql.matches( ".*\\*.*" ) ){
					display.showSelectAll( resultset );
				}else{
					display.showSelect( sql , resultset );
				}
			}else if( "SHOW TABLES".equals(sql) ){ //SQL文がSHOW TABLESの場合
				display.showTables( resultset );
			}
			
		
		}catch( SQLException e ){
			System.out.println( "MySQLに接続できませんでした." );
		}/*finally{
			if( connection != null ){
				try{
					preparedstatement.close();
					connection.close();
				}catch( SQLException e ){
					System.out.println("MySQLをクローズできませんでした。");
				}
			}
			
		}
		*/
	}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/7
	追加・更新系SQL文の実行用メソッド
	------------------------------------------------------------------------
	*/
	public void updateDB(){
		try{
			System.out.println( sql );
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.executeUpdate();
			
			if( sql.matches( "INSERT INTO.*" ) ){
				System.out.println( "データを追加しました。" );
			}else if( sql.matches( "UPDATE.*" ) ){
				System.out.println( "データを更新しました。" );
			}else if( sql.matches( "DELETE.*" ) ){
				System.out.println( "データを削除しました。" );
			}
			
		}catch( SQLException e ){
			System.out.println( "更新処理が実行できませんでした。" );
		}
	
	}
	
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/7
	ResultSet型を返すメソッド
	------------------------------------------------------------------------
	*/
	public ResultSet getResultSet(){
		try{
			String sql = "SELECT * FROM soft";
			//preparedstatementを使います
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery(sql);
		}catch( SQLException e ){
			System.out.println( "MySQLに接続できませんでした." );
		}
		
		return resultset;
	}
	/*
	----------------------------------------------------------
	@author testakaunto 2016/6/7
	ResultSetMetaData型を返すメソッド
	SQL文はあらかじめセットしておく。
	----------------------------------------------------------
	*/
	public ResultSetMetaData getMetaData(){
		try{
			String sql = "SELECT * FROM soft";
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery(sql);
			//resultsetmetadataに代入します
			resultsetmetadata = resultset.getMetaData();
			//**カラム名が取得できているかテスト（下記forブロックは削除予定）**
			/*
			for( int i = 1 ; i < ( resultsetmetadata.getColumnCount() + 1 ) ; i++ ){
				String columnname = resultsetmetadata.getColumnName(i);
				System.out.println( "カラム名：" + columnname );
			}
			*/
		
		}catch( SQLException e ){
			System.out.println( "MySQLに接続できませんでした." );
		}
		return resultsetmetadata;
}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/5
	表示モジュールを作成しましたが、connectDB()から表示メソッドを
	呼び出すためconnectDB()内でクローズできるので、
	下記メソッドは不要かもしれません。
	@editor testakaunto 2016/6/7
	preparedstatement.close()を追加
	ResultSet型またはResultSetMetaData型を返す場合、それぞれの
	メソッド内でクローズできないので、
	Controlクラスのmainloopにfalseを代入する直前に記述するのに使うと良いです。
	------------------------------------------------------------------------
	*/
	public void connectClose(){
		if( connection != null ){
			try{
				if( preparedstatement != null ){
					preparedstatement.close();
				}
				connection.close();
			}catch( SQLException e ){
				System.out.println("MySQLをクローズできませんでした。");
			}
		}
	}
	
	//コンストラクタでsqlをセットしても良いですが、セッターも用意しています。
	public void setSql( String sql ){
		this.sql = sql;
	}
	
	//sqlのゲッターです。
	public String getSql(){
		return this.sql;
	}
	
	
}