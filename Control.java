package control;

import java.util.List;
import java.util.ArrayList;
import java.io.*;
/*import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;*/
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


import main.ItemManageMain;
//import connect.Connect2;
import connect.Connect3;


/*
@author testakaunto 2016/6/3
@editer yakitori21	2016/6/4
@editor testakaunto 2016/6/7
@editor testakaunto 2016/6/9
*/

public class Control{
	//-----------------------------------------------------------------------
	// コンストラクタ
	//
	//@author yakitori21 2016/6/4
	// 引数	m_ins :	メインループの終了フラグを制御するためのインスタンス
	// 		c_ins : データベース情報を取得する為の通信クラスのインスタンス
	//-----------------------------------------------------------------------
	public Control(ItemManageMain m_ins/*,Connect c_ins*/){
		// 標準入力オブジェクトRを作成
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.windowNo = TOPMENU; // トップメニューの番号を設定。

		// インスタンスを登録
		this.main = m_ins;		// メインクラス
		this.connect = new Connect3();	// データベース通信クラス
		
		// データベース情報を取得
		//this.rs = con.getメソッド(仮)
		//this.rsmd = con.getメソッド(仮)
	}
	
	/* フィールド */

	//private ResultSet rs;			// データベースの内部情報を持つ
	//private ResultSetMetaData rsmd;	// データベースのカラム情報を持つ
	//private Connect con;			// データベース情報を取得する為のインスタンス
	private ItemManageMain main;	// 終了フラグ制御するインスタンス
	private BufferedReader reader;	// 標準入力用
	private int windowNo;			// 表示する画面番号
	private Connect3 connect;
	/* 定数 */

	// 画面操作
	private static final int TOPMENU = 0;		// トップメニュ:0
	private static final int TABLE_SELECT = 1;	// テーブル選択画面:1
	private static final int ADD_DATA = 2;		// データ追加画面:2

	// 機能
	private static final int END = 0;
	private static final int ALL_DRAW = 1;
	private static final int ADD = 2;
	private static final int SERACH = 3;
	
	/* メソッド */
	
	//----------------------------------------
	// 画面番号に応じて表示を分ける
	// メインループがこのメソッドを呼び出し
	// 画面を更新する
	//
	//@author yakitori21 2016/6/4
	//----------------------------------------
	public void changeWinodow() throws SQLException {
		// 画面番号をもとに画面を呼び出す。
		switch(this.windowNo){
			case TOPMENU:	// トップメニュー 
				this.topMenu();	
				break;	
			case TABLE_SELECT:	// テーブルセレクト画面
				this.selectTable();
				break;
			case ADD_DATA:	// データ追加画面
				this.addData();
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
	//case ALL_DRAW でConnect3を呼び出し
	//sqlをセットするコードを追加
	//---------------------------------
	public void topMenu() throws SQLException {
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
							System.out.println( sql );
							this.connect.setSql( sql );	// 通信クラスにSQL文をセット
							this.connect.connectDB();	// データベースにSQL文を送信
							break;
						case ADD:	// 追加機能
							//this.windowNo = ADD_DATA;	// 画面変更
							break;
						case SERACH: // 抽出・検索機能-未実装(※)
							sql = search();
							System.out.println( sql );
							this.connect.setSql( sql );
							this.connect.connectDB();
							break;
						default:	// 事前に弾いてるけど一応
							break;
					}
					break;
				}else{
					System.out.println("入力値が正しくありません。0-3の数値を入力してください");
				}
			}catch(NumberFormatException e){
				//System.out.println(e);
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
					windowNo = TOPMENU;
				}
				System.out.println("入力を確認しました。トップメニューに戻ります");
				windowNo = TOPMENU;
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
	//-------------------------------------------------------------
	public void addData(){
		String line = "";			// 標準入力
		String columnName = "";		// カラム名
		String sql = "INSERT INTO soft VALUES(";
		System.out.println("Itemテーブルにデータを追加します。");
		System.out.println("");

		try{
			while(true){
				line = reader.readLine();
				if(line == null){	// ユーザーが入力をキャンセル
					System.out.println("入力がキャンセルされました。トップメニューへ移動します");
					windowNo = TOPMENU;
				}
				System.out.println("入力を確認しました。トップメニューに戻ります");
				windowNo = TOPMENU;
				break;
			}
		}catch(IOException e){
			System.out.println(e);
		}
		// カラム名を取得
		/*label:	// ラベルbreak
		for (int i = 1; i <= this.rsmd.getColumnCount(); i++) {
			columnName = this.rsmd.getColumnName(i);
   			System.out.println("カラム名:"+columnName+"に追加するデータを入力してください");
			
			// 適切なデータを入力しないかぎり繰り返す
			while(true){
				line = reader.readLine();
				if(line == null){	// ユーザーが入力をキャンセル
					System.out.println("入力がキャンセルされました。トップメニューへ移動します");
					windowNo = TOPMENU;
					break label;	// ラベルbreakでfor文自体を脱出。
				}
				if(this.chkConsistency(columnName,line)){	// 文字入力が正しいか？
					if(i!=rsmd.getColumnCount()){	// 最後のカラム入力でないなら
						sql = "\'"+line+"\' ,";
					}else{
						sql = "\'"+line+"\'";
					}
					break;	// 入力の一巡終了。
				}
			}
		}
		sql = sql+");";	// SQL文の終端を設定 
		*/
	}
	
	//--------------------------------------
	// 入力値の整合性を確認する-未完成(※)
	//@author yakitori21 2016/6/4
	// 引数		colum : 検索するカラム名
	// 			st	  : 検索する対象文字列
	//--------------------------------------
	public boolean chkConsistency(String colum,String st){
		boolean chk = false;
		if(colum == "ID"){		// 商品ID
			chk = st.matches("A[0-9]{4}");	// Aと(0～9)の4文字構成
		}else if(colum == "NAME"){		// 商品名
			chk = st.matches(".{1,30}");	// 1文字以上30文字以下
		}else if(colum == "YOMI"){		// 商品名ヨミ
			chk = st.matches("[ァ-ヶ｡-ﾟ]{1,30}"); // 全角カタカナまたは半角で1文字以上30文字以下
		}else if(colum == "PRICE"){	// 価格
			try{
				int input = Integer.parseInt(st);
				chk = (st.matches("[1-5]*[0-9]{1,4}") && (input <= 50000) );	// 50000円以上は登録不可
			}catch(NumberFormatException e){
				System.out.println(e);
			}
		}else if(colum == "GENRE"){
			chk = st.matches(".{1,10}");
		}else if(colum == "RELE"){
			chk = st.matches("20[0-9]{2}/[1]*[1-9]/[1-3]*[1-9]");
		}else if(colum == "PLTF"){
		}else if(colum == "CODI"){
		}else if(colum == "STOCK"){
		}
		return chk;
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
		ResultSet resultset = connect.getResultSet();
		ResultSetMetaData resultsetmetadata = connect.getMetaData();
		List<String> columnlist = new ArrayList<String>();
		List<String> truecolumnlist = new ArrayList<String>();
		
		columnlist.add( "商品ID" );
		columnlist.add( "商品名" );
		columnlist.add( "ヨミガナ" );
		columnlist.add( "価格" );
		columnlist.add( "ジャンル" );
		columnlist.add( "発売日" );
		columnlist.add( "プラットフォーム" );
		columnlist.add( "状態" );
		columnlist.add( "在庫" );
		
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		
		while( searchloop ){
			if( k < 1 ){
				System.out.println( "抽出するデータの項目を選び、数字を入力してください。" );
			}else{
				System.out.println("他に抽出するデータ項目があれば、数字を入力してください。");
				System.out.println( selectcolumn );
			}
			for( int i = 0 ; i < columnlist.size() ; i++ ){
				System.out.println( "" + i + "：" + columnlist.get(i));
			}
			System.out.println( "" + columnlist.size() + "：" + "＜終了＞" );
			try{
				String str = reader.readLine();
				int num = Integer.parseInt( str );
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
		
		sql += "FROM soft";
		return sql;
	}
}