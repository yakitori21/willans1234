package control;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import main.ItemManageMain;
import connect.Connect3;

/*
@author testakaunto 2016/6/3
@editor yakitori21	2016/6/4
@editor testakaunto 2016/6/7
@editor yakitori21	2016/6/9
@editor testakaunto 2016/6/9
@editor yakitori21	2016/6/10
*/

//@editor testakaunto 2016/6/9
//ResultSetMetaData resultsetmetadata -> 変数名resultsetmetadataに変更
public class Control{
	/* フィールド */
	private ResultSetMetaData resultsetmetadata;	// データベースのカラム情報を持つ
	private ItemManageMain main;	// 終了フラグ制御するインスタンス
	private BufferedReader reader;	// 標準入力用
	private int windowNo;			// 表示する画面番号
	private Connect3 connect;		// 通信モジュール

	/*
	------------------------------------------------------------
	@editor testakaunto 2016/6/9
	search()の追加により、各メソッドにthrows SQLExceptionを追加
	------------------------------------------------------------
	*/


	//-----------------------------------------------------------------------
	// コンストラクタ
	//
	//@author yakitori21 2016/6/4
	// 引数	m_ins :	メインループの終了フラグを制御するためのインスタンス
	// 		c_ins : データベース情報を取得する為の通信クラスのインスタンス
	//@editor testakaunto 2016/6/9
	//-----------------------------------------------------------------------
	public Control(ItemManageMain m_ins/*,Connect c_ins*/){
		// 標準入力オブジェクトを作成
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.windowNo = TOPMENU; // トップメニューの番号を設定。

		// インスタンスを登録
		this.main = m_ins;		// メインクラス
		this.connect = new Connect3();	// データベース通信クラス
		
		// インスタンス初期化
		this.resultsetmetadata = null;		// ResultSetMetaDataを初期化
	}
	
	/* 定数 */

	// 画面操作
	private static final int TOP_MENU = 0;		// トップメニュ:0
	private static final int TABLE_SELECT = 1;	// テーブル選択画面:1
	private static final int ADD_MENU = 2;		// データ追加画面:2
	private static final int SERACH_MENU = 3;	// 検索画面:3
	private static final int DELETE_MENU = 4;	// 削除画面:4
	private static final int UPDATE_MENU = 5;	// 更新画面:5

	// 機能
	private static final int END = 0;			// 終了
	private static final int ALL_DRAW = 1;		// 全表示
	private static final int ADD = 2;			// 追加
	private static final int SERACH = 3;		// 検索
	private static final int DELETE = 4;		// 削除
	private static final int UPDATE = 5;		// 更新
	
	/* メソッド */
	
	//-------------------------------------------------------
	// 画面番号に応じて表示を分ける
	// メインループがこのメソッドを呼び出し
	// 画面を更新する
	//
	//@author yakitori21 2016/6/4
	//@editor testakaunto 2016/6/10
	//case SERACHのsearch()にSQL文を受けるString sqlと
	//connectDB()、TOPMENUに戻る処理を追加
	//-------------------------------------------------------
	public void changeWindow() throws SQLException , IOException {
		String sql = "";
		// 画面番号をもとに画面を呼び出す。
		switch(this.windowNo){
			case TOP_MENU:	// トップメニュー 
				this.topMenu();	
				break;	
			case TABLE_SELECT:	// テーブルセレクト画面
				this.selectTable();
				break;
			case ADD_MENU:	// データ追加画面
				System.out.println( "*****テーブル名が変更されているか注意してください！！*****" );
				sql = this.addData();
				break;
			case SERACH_MENU:	// 検索画面
				sql = this.search();
				connect.setSql( sql );
				connect.connectDB();
				break;
			case DELETE_MENU:	// 削除画面
				break;
			case UPDATE_MENU:	// 更新画面
				this.update();
				break;
			default:
				System.out.println("Error:画面遷移異常によりシステムを終了します。");
				this.main.setLoopEnd();	// メインループ終了
				break;
		}
	}

	//---------------------------------
	// トップメニュでの操作
	// 
	//@author yakitori21 2016/6/4
	//@editor testakaunto 2016/6/7
	//case ALL_DRAW でConnect2を呼び出し
	//sqlをセットするコードを追加
	//---------------------------------
	public void topMenu() {
		String line = "";	// 標準入力を受け取る変数
		System.out.println();
		System.out.println( "メニューを選んでください。" );
		System.out.println();
		System.out.println( "0：商品管理システムの終了" );
		System.out.println( "1：テーブルの商品を全て表示する" );
		System.out.println( "2：テーブルにデータを追加する" );
		System.out.println( "3：条件を指定してテーブル中のデータを表示する" );
		System.out.println();
		
		// 入力が完了されるまで
		while(true){
			try{
				if(( line = this.reader.readLine())==null ){	// 入力
					System.out.println("入力キャンセルされました。プログラムを終了します");
					this.main.setLoopEnd();
					break;
				}

				// 入力制限[0-3]
				if(line.matches("[0-3]")){
					int input = Integer.parseInt(line);	// 数値型変換
					// 入力値に応じて処理を分岐
					switch(input){
						case END:	// 終了
							this.main.setLoopEnd();  // メインループを脱出するフラグ制御
							break;
						case ALL_DRAW: 	// テーブルの商品を全て表示する
							String sql = "SELECT * FROM soft;";
							System.out.println(sql);
							this.connect.setSql(sql);	// 通信クラスにSQL文をセット
							this.connect.connectDB();	// データベースにSQL文を送信
							break;
						case ADD:	// 追加機能
							this.windowNo = ADD_MENU;	// 画面変更
							break;
						case SERACH: // 抽出・検索機能
							this.windowNo = SERACH_MENU;	// 画面変更
							break;
						case DELETE:	// 削除機能
							//this.windowNo = DELETE_MENU;	
							System.out.println("ウィンドウの切り替え処理を書く");
						case UPDATE:
							this.windowNo = UPDATE_MENU;
							break;
						default:	// 事前に弾いてるけど一応
							break;
					}
					break;
				}else{
					System.out.println("入力値が正しくありません。0-3の数値を入力してください");
				}
			}catch(NumberFormatException e){
				System.out.println(e);
			}catch(IOException e){
				System.out.println(e);
			}
		}
	}

	//-------------------------------
	// テーブル選択画面-未実装(※)
	// 
	//@author yakitori21 2016/6/4
	//-------------------------------
	public void selectTable(){
		String line = "";
		System.out.println("テーブル画面を表示しました。");

		try{
			while(true){
				line = reader.readLine();
				if(line == null){	// ユーザーが入力をキャンセル
					System.out.println("入力がキャンセルされました。トップメニューへ移動します");
					windowNo = TOP_MENU;
				}
				System.out.println("入力を確認しました。トップメニューに戻ります");
				windowNo = TOP_MENU;
				break;
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	//-------------------------------------------------------------
	// データ追加画面
	// ユーザーが入力を行い、入力データをデータベースに登録する。
	// 内部で入力データを作成しConnectオブジェクトに送信する
	//@author yakitori21 2016/6/4
	//@editor yakitori21 2016/6/9
	//-------------------------------------------------------------
	public String addData() throws SQLException , IOException {
		String sql = "INSERT INTO soft2 VALUES(";
		System.out.println("Itemテーブルにデータを追加します。");
		System.out.println("");

		resultsetmetadata = connect.getMetaData();	// カラム情報を取得
		
		// カラム名を取得
		label:	// ラベルbreak
		for (int i=1; i<=this.resultsetmetadata.getColumnCount(); i++){
			String columnName = this.resultsetmetadata.getColumnName(i);
			System.out.println("カラム名:"+relabelString(columnName)+"データを入力してください");
			
			// 適切なデータを入力しないかぎり繰り返す
			while(true){
				String line = reader.readLine();
				if(line == null){	// ユーザーが入力をキャンセル
					System.out.println("入力がキャンセルされました。トップメニューへ移動します");
					this.windowNo = TOP_MENU;
					break label;	// ラベルbreakでfor文自体を脱出。
				}
				if(this.chkConsistency(columnName,line)){	// 文字入力が正しいか？
					if(i!=resultsetmetadata.getColumnCount()){	// 最後のカラム入力でないなら
						sql += "\'"+line+"\' ,";
					}else{
						sql += "\'"+line+"\'";
					}
					break;	// 入力の一巡終了。
				}
			}
		}
		sql = sql+");";	// SQL文の終端を設定 
		System.out.println(sql);

		this.windowNo = TOP_MENU;	// 追加後はトップメニューに遷移
		return sql;					// 作成したSQL文を返す。	
	}

	/*
	--------------------------------------------------
	@author testakaunto 2016/6/8
	データを抽出して表示するSQL文
	を作成するメソッドを作成しました。
	--------------------------------------------------
	*/
	
	public String search() throws SQLException {
		int k = 0;//カウンタ変数
		
		boolean searchloop = true;
		String sql = "SELECT";
		String selectcolumn = "選択した項目：";
		//ResultSet resultset = connect.getResultSet();
		resultsetmetadata = connect.getMetaData();
		//表示用のカラム名を入れるcolumnlistと実際のカラム名を入れるtruecolumnlistを作成
		List<String> columnlist = new ArrayList<String>();
		List<String> truecolumnlist = new ArrayList<String>();
		
		//columnlistに表示用のカラム名を入れます。
		columnlist.add( "商品ID" );
		columnlist.add( "商品名" );
		columnlist.add( "ヨミガナ" );
		columnlist.add( "価格" );
		columnlist.add( "ジャンル" );
		columnlist.add( "発売日" );
		columnlist.add( "プラットフォーム" );
		columnlist.add( "状態" );
		columnlist.add( "在庫" );
		
		//truecolumnlistに実際のカラム名を入れます。
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		//終了を選択するとループを抜けます。
		while( searchloop ){
			if( k < 1 ){//ループが1回目かどうかで表示を変えます
				System.out.println( "抽出するデータの項目を選び、数字を入力してください。" );
			}else{
				System.out.println("他に抽出するデータ項目があれば、数字を入力してください。");
				System.out.println( selectcolumn );//既に選択したデータを表示します。
			}
			for( int i = 0 ; i < columnlist.size() ; i++ ){//表示用カラム名を数字を付けて表示します
				System.out.println( "" + i + "：" + columnlist.get(i));
			}
			//終了は最後のカラム名に表示します。
			System.out.println( "" + columnlist.size() + "：" + "＜選択終了＞" );
			try{
				String str = reader.readLine();
				int num = Integer.parseInt( str );
				//入力チェックをしながらsqlにカラム名を追加します。
				if( num < 0 || num > columnlist.size() ){
					System.out.println( "入力された数値が正しくありません。入力をやり直してください。" );
					continue;
				}else if( num < columnlist.size() ){
					if( k > 0 ){
						sql += ",";
						selectcolumn += "、";
						
					}
					sql += " " + truecolumnlist.get( num ) + " ";
					selectcolumn += columnlist.get( num );
					//columnlist、truecolumnlistは要素番号が対応しているので、両方とも同じ番号でremoveします。
					columnlist.remove( num );
					truecolumnlist.remove( num );
				}else if( num == columnlist.size() ){
					System.out.println( "検索条件の追加を終了し、結果を表示します。" );
					searchloop = false;
					break;
				}
				
				
			}catch( NumberFormatException e ){
				System.out.println( "数値以外が入力されました。入力をやり直してください。" );
				continue;
			}catch( IOException e ){
				System.out.println( "入力エラーが発生しました。入力をやり直してください。" );
				continue;
			}
			k++;
		}
		windowNo = TOP_MENU;
		//最後にsql文の文末を付け加え、sqlを返します。
		sql += "FROM soft";
		return sql;
	}

	//--------------------------------------------
	// データ検索画面
	// 検索するカラムをユーザーに指定してもらう
	//
	//@author yakitori21 2016/6/9
	//--------------------------------------------
	/*public void SearchData(){
		String line;
		ArrayList<String> list = new ArrayList<String>();
		this.resultsetmetadata = connect.getMetaData();	// カラム情報を取得
		
		// カラムデータを登録
		for(int i=1;i<=this.getColumnCount();i++){
			String columnName = this.resultsetmetadata.getColumnName(i);
			list.add(columnName);
		}
		
		// ユーザー入力
		while((line = reader.readLine())!=null){
			

			// 入力案内
			System.out.println("以下から検索する番号を選び入力してください。");
			for(int i=0;i<list.size();i++){
				System.out.print(i+":"+list.get()+"\t");
			}
		}
	}*/
	
	//--------------------------------------
	// 入力値の整合性を確認する
	//@author yakitori21 2016/6/4
	//@editor yakitori21 2016/6/9
	// 引数		column : 検索するカラム名
	// 			st	   : 検索する対象文字列
	//--------------------------------------
	public boolean chkConsistency(String column,String st){
		boolean chk = false;
		if(column.equals("ID")){		// 商品ID
			chk = st.matches("A[0-9]{4}");	// Aと(0～9)の4文字構成
			if(!chk){
				System.out.println("入力例 A0010 Aと(0～9)の4文字構成で入力してください");
			}
		}else if(column.equals("NAME")){		// 商品名
			chk = st.matches(".{1,30}");	// 1文字以上30文字以下
			if(!chk){
				System.out.println("1文字以上30文字以下で入力してください");
			}
		}else if(column.equals("YOMI")){		// 商品名ヨミ
			chk = st.matches("[^0-9a-zA-Zぁ-ゞ一-龠]{1,30}"); // 記号または全角、半角カタカナで1文字以上30文字以下
			if(!chk){
				System.out.println("記号または全角、半角カタカナで1文字以上30文字以下で入力してください");
			}
		}else if(column.equals("PRICE")){	// 価格
			int input = ChgInt(st);
			chk = (input >= 0 && input <= 50000);	// 0円以上50000円以下
			if(!chk){
				System.out.println("0円以上50000円以下の数値で入力してください");
			}
		}else if(column.equals("GENRE")){
			chk = st.matches(".{1,10}");	// 1文字以上10文字以下
			if(!chk){
				System.out.println("1文字以上10文字以下で入力してください");
			}
		}else if(column.equals("RELE")){	// timeメソッドにデータを入れてフォーマットが間違っていたらはじく(※)
			chk = st.matches("20[0-9]{2}-[1]*[0-9]-[1-3]*[0-9]");
			if(!chk){
				System.out.println("例:2019-10-30のように年号-月-日の形式で入力してください");
			}
		}else if(column.equals("PLTF")){	// 1文字以上10文字以下
			chk = st.matches(".{1,10}");
			if(!chk){
				System.out.println("1文字以上10文字以下で入力してください");
			}
		}else if(column.equals("CODI")){	// 1文字以上10文字以下
			chk = st.matches(".{1,10}");
			if(!chk){
				System.out.println("1文字以上10文字以下で入力してください");
			}
		}else if(column.equals("STOCK")){
			int input = ChgInt(st);
			chk = (input >= 0 && input < 100);	// 在庫100以上は登録不可
			if(!chk){
				System.out.println("0以上99以下の数値を入力してください");
			}
		}
		return chk;
	}

	//--------------------------------------------------
	// カラム名を表示用の新しい名前に変える
	// 
	//@author yakitori21 2016/6/9
	//引数	st	:	カラム名
	//戻り値	:	新しい名前
	//---------------------------------------------------
	public String relabelString( String st ){
		String res_st = "";
		if(st.equals("ID")){
			res_st = "商品ID";
		}else if(st.equals("NAME")){
			res_st = "商品名";
		}else if(st.equals("YOMI")){
			res_st = "ヨミガナ";
		}else if(st.equals("PRICE")){
			res_st = "価格";
		}else if(st.equals("GENRE")){
			res_st = "ジャンル";
		}else if(st.equals("RELE")){
			res_st = "リリース";
		}else if(st.equals("PLTF")){
			res_st = "プラットフォーム";
		}else if(st.equals("CODI")){
			res_st = "状態";
		}else if(st.equals("STOCK")){
			res_st = "在庫";
		}
		return res_st;
	}

	//-----------------------------------
	// データベースの事後処理
	// データベースの開放処理を呼び出す
	//
	//@author yakitori21 2016/6/9
	//------------------------------------
	public void closeDatabase(){
		connect.connectClose();	// データベース切断
	}

	//------------------------------------
	// 文字列型の整数型に変換
	// 例外記述の省略
	//
	//@author yaktiori212016/6/9
	//引数	st	:	変換する文字列
	//戻り値	:	変換後の整数
	//------------------------------------
	public static int ChgInt(String st){
		try{
			int num = Integer.parseInt(st);
			return num;
		}catch(NumberFormatException e){
			System.out.println(e);
		}
		return 0;
	}
	/*
	--------------------------------------------
	@author testakaunto 2016/6/10
	データを更新するSQL文を作成するメソッド
	--------------------------------------------
	*/
	
	public String update() throws SQLException {
		boolean idloop = true;
		boolean columnloop = true;
		boolean dataloop =true;
		String sql = "UPDATE soft2 SET";
		String id = "";
		String column = "";
		String data = "";
		
		connect.setSql("SELECT * FROM soft2");
		ResultSet resultset = connect.getResultSet();
		resultsetmetadata = connect.getMetaData();
		List<String> columnlist = new ArrayList<String>();
		List<String> truecolumnlist = new ArrayList<String>();
		
		//columnlistに表示用のカラム名を入れます。
		columnlist.add( "商品ID" );
		columnlist.add( "商品名" );
		columnlist.add( "ヨミガナ" );
		columnlist.add( "価格" );
		columnlist.add( "ジャンル" );
		columnlist.add( "発売日" );
		columnlist.add( "プラットフォーム" );
		columnlist.add( "状態" );
		columnlist.add( "在庫" );
		
		//truecolumnlistに実際のカラム名を入れます。
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		
		for( int i = 0 ; i < columnlist.size() ; i++ ){//表示用カラム名を数字を付けて表示します
				System.out.println( "" + i + "：" + columnlist.get(i));
			}
		
		
		while( idloop ){
			try{
				System.out.println( "更新したいデータの商品IDを入力してください。" );
				id = reader.readLine();
				idloop = chkConsistency( "ID" , id );
				//id入力が正しいかチェック
				if( idloop != true ){
					System.out.println( "入力した値が正しくありません。" );
					continue;
				}
				connect.setSql("SELECT ID FROM soft");
				resultset = connect.getResultSet();
				//指定したIDがテーブル上に存在するかチェック
				while( resultset.next() ){
					String iddata = resultset.getString( "ID" );
					if( !id.equals( iddata ) ){
						System.out.println( "存在しないデータです。" );
						break;
					}
				
				}
			}catch( IOException e ){
				System.out.println( "入力エラーが発生しました" );
				continue;
			}
		}
		connect.setSql("SELECT * FROM soft2");
		System.out.println( "次に更新するデータ項目を選択し、数字を入力してください。" );
		while( columnloop ){
			try{
				for( int i = 0 ; i < columnlist.size() ; i++ ){//表示用カラム名を数字を付けて表示します
					System.out.println( "" + i + "：" + columnlist.get(i) );
				}
				String strnum = reader.readLine();
				int num = Integer.parseInt( strnum );
				column = " " + truecolumnlist.get(i) + " ";
			}catch( IOException e ){
				System.out.println( "入力エラーが発生しました" );
				continue;
			}catch( NumberFormatException e ){
				System.out.println( "数値以外が入力されました。入力をやり直してください。" );
				continue;
			}
		}
		System.out.println( "最後にデータの値を入力してください。" );
		
		sql += column + "=" data + "WHERE ID =" + id;
	}
	
}


}

