package main;

import java.sql.SQLException;
import java.io.IOException;
import control.Control;

//import input.Input;
//import java.io.IOException;
/*
@author testakaunto 2016/6/3
@editer yakitori21 2016/6/4
@editer yakitori21 2016/6/7
@editer yakitori21 2016/6/9
@editor yakitori21 2016/6/10
*/
//@autor testakaunto 
//各メソッドにthrows SQLException , IOExceptionを追加
public class ItemManageMain {
	/* フィールド */
	private Control ctrl; 	// 操作オブジェクト
	private boolean loopflg;	// 終了フラグ

	//------------------------------------------------
	// コンストラクタ
	//
	// フィールドの初期化、オブジェクトの生成を行う
	//@author yakitori21 2016/6/5
	//------------------------------------------------
	public ItemManageMain() throws SQLException {
		// フィールドの初期化
		loopflg = true;

		System.out.println("mainオブジェクト作成");
	
		// オブジェクトの生成
		ctrl = new Control(this); // 操作
	}
	
	/* メソッド */

	// メイン処理
	public static void main( String[] args ) throws SQLException , IOException{
		// 自身のオブジェクトを作成
		// (自身の参照値を他オブジェクトに渡せない為)
		ItemManageMain mainIns = new ItemManageMain();

		mainIns.mainLoop();	// ループ処理を呼ぶ
		

		System.exit(0);
	}
	
	//------------------------------------------------
	// ループメソッド
	//
	// 終了されない限りループを繰り返す仕組み
	//@author yakitori21 2016/6/5
	//------------------------------------------------
	public void mainLoop() throws SQLException , IOException {
		System.out.println( "商品管理システムへようこそ！" );
		
		// メインループ
		while( this.loopflg ){
			ctrl.changeWindow();	// 画面切替メソッドを呼び出す
		}
		ctrl.closeDatabase();
		System.out.println("");
		System.out.println("--------------------------");
		System.out.println("プログラムを終了します。");
	}

	// メインループの脱出
	public void setLoopEnd(){
		this.loopflg = false;
	}
}
