package display;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


import connect.Connect2;
import connect.Connect3;

/*
@author testakaunto 2016/6/5

----------------------------------------------------
Connectクラスに表示のコードが冗長になったため、
Displayクラスに表示の処理を分割
・SELECT * ... 
・SELECT [カラム名1, カラム名2, ...] ...
・SHOW TABLES
上記SQL文それぞれでメソッドごとに処理を分けました。
----------------------------------------------------
*/

public class Display{
	private ResultSet resultset;
	private ResultSetMetaData resultsetmetadata;
	private List<String> showlist;
	
	//コンストラクタ群
	public Display(){
	
	}
	public Display( ResultSet resultset ){
		this.resultset = resultset;
		
	}
	
	public void showSelectAll( ResultSet resultset ) throws SQLException{
		//表示するデータを入れておくList型のshowlistを宣言
		List<String> showlist = new ArrayList<String>();
		
		//商品名、ヨミガナは15文字分の枠を用意するため、最大文字数MAX_LENGTHを15で初期化して宣言
		final int MAX_LENGTH = 15;
		System.out.println( "商品ID\t" + "    商品名\t\t\t" + "    ヨミガナ\t\t\t" + "価格\t" + "ジャンル\t\t\t" + "　発売日\t" + "プラットフォーム\t" + "状態\t" + "在庫\t" );
		while( resultset.next() ){
			//カラム名を入れるString型の変数を準備、int型変数はgetIntを受けるために用意
			String id = resultset.getString( "ID" );
			String name = resultset.getString( "NAME" );
			String yomi = resultset.getString( "YOMI" );
			int price = resultset.getInt( "PRICE" );
			String strprice = String.valueOf( price );
			String genre = resultset.getString( "GENRE" );
			String rele = resultset.getString( "RELE" );
			String pltf = "\t" + resultset.getString( "PLTF" );
			String codi = resultset.getString( "CODI" );
			int stock = resultset.getInt( "STOCK" );
			String strstock = String.valueOf( stock );
			
			//以下、データを表示用に整形
			if( name.length() < MAX_LENGTH ){
				if( name.length() < 4 ){
					name += "\t\t";
				}else if( name.length() < 8 ){
					name += "\t";
				}else if( "グランドカート2".equals( name ) ){//グランドカート2のみ表示が揃わないので条件を付けたしました。
					name += "\t\t";
				}
				for( int i = 0 ; i < ( MAX_LENGTH - name.length() ) ; i++ ){
					name += "　";
				}
			}
			if( yomi.length() < MAX_LENGTH ){
				if( yomi.length() < 4 ){
					yomi += "\t\t";
				}else if( yomi.length() < 8 ){
					yomi += "\t";
				}
				for( int i = 0 ; i < ( MAX_LENGTH - yomi.length() ) ; i++ ){
					yomi += "　";
				}
			}
			if( genre.length() < 4 ){
				genre += "\t\t";
			}else if( genre.length() < 8 ){
				genre += "\t";
			}
			pltf += "\t";
			//リストに整形したデータを追加
			showlist.add( id );
			showlist.add( name );
			showlist.add( yomi );
			showlist.add( strprice );
			showlist.add( genre );
			showlist.add( rele );
			showlist.add( pltf );
			showlist.add( codi );
			showlist.add( strstock );
			for( int i = 0 ; i < showlist.size() ; i++ ){
				System.out.print( showlist.get(i) + "\t" );
			}
			System.out.println();
			System.out.println();
			//次の行に移るためshowlistをクリア
			showlist.clear();
		}
	}
	/*
	----------------------------------------------------------------
	@editor testakaunto 2016/6/7
	if文にヨミガナがなかったので追加
	Connect型持つのインスタンス変数sqlとresultset
	sqlはカラム名が含まれているかの判別に利用
	指定するカラム名の文字列に
	違う文字列が結合(example."NAME"と"MAKERNAME"など)する可能性への
	対策として、検索するカラム名の前後に半角空白を入れて検索する
	よって、SQL文生成側でも半角空白を入れて生成してもらうようにする
	----------------------------------------------------------------
	*/
	public void showSelect( String sql , ResultSet resultset ) throws SQLException{			
		//表示するためのリストをインスタンス化
		showlist = new ArrayList<String> ();
		int k = 0;
		while( resultset.next() ){
			//カラム名を入れるString型の変数を準備、int型変数はgetIntを受けるために用意
			String id = "";
			String name = "";
			String yomi = "";
			String strprice = "";
			String genre = "";
			String rele = "";
			String pltf = "";
			String codi = "";
			String strstock = "";
			int price = 0;
			int stock = 0;
			
			//商品名、ヨミガナは15文字分の枠を用意するため、最大文字数MAX_LENGTHを15で初期化して宣言
			final int MAX_LENGTH = 15;
			
			System.out.println();
			
			//SQL文中のカラム名を検索し、該当カラムがあればshowlistに追加
			//int型はString型に変換
			if( sql.matches( ".* ID .*" ) ){
				id += resultset.getString( "ID" );
				showlist.add( id );
				if( k < 1 ){
					System.out.print( "商品ID\t" );
				}
			}
			if( sql.matches( ".* NAME .*" ) ){
				name += resultset.getString( "NAME" );
				//文字数をMAX_LENGTHと比較し、15文字以下であれば、文字数に応じてTABを行う
				if( name.length() < MAX_LENGTH ){
					if( name.length() < 4 ){
						name += "\t\t";
					}else if( name.length() < 8 ){
						name += "\t";
					}else if( "グランドカート2".equals( name ) ){//グランドカート2のみ表示が揃わないので条件を付けたしました。
						name += "\t\t";
					}
					for( int i = 0 ; i < ( MAX_LENGTH - name.length() ) ; i++ ){
						name += "　";
					}
				}
				showlist.add( name );
				if( k < 1 ){
					System.out.print( "    商品名\t\t\t" );
				}
			}
			if( sql.matches( ".* YOMI .*" ) ){
				yomi += resultset.getString( "YOMI" );
				//nameのifブロックと同じ
				if( yomi.length() < MAX_LENGTH ){
					if( yomi.length() < 4 ){
						yomi += "\t\t";
					}else if( yomi.length() < 8 ){
						yomi += "\t";
					}
					for( int i = 0 ; i < ( MAX_LENGTH - yomi.length() ) ; i++ ){
						yomi += "　";
					}
				}
				showlist.add( yomi );
				if( k < 1 ){
					System.out.print( "    ヨミガナ\t\t\t" );
				}
			}
			if( sql.matches( ".* PRICE .*" ) ){
				price = resultset.getInt( "PRICE" );
				strprice += String.valueOf( price );
				showlist.add( strprice );
				if( k < 1 ){
					System.out.print( "価格\t" );
				}
			}
			if( sql.matches( ".* GENRE .*" ) ){
				
				genre += resultset.getString( "GENRE" );
				if( genre.length() < 4 ){
					genre += "\t\t";
				}else if( genre.length() < 8 ){
					genre += "\t";
				}
				showlist.add( genre );
				if( k < 1 ){
					System.out.print( "ジャンル\t\t" );
				}
			}
			if( sql.matches( ".* RELE .*" ) ){
				rele += resultset.getString( "RELE" );
				showlist.add( rele );
				if( k < 1 ){
					System.out.print( "　発売日\t" );
				}
			}
			if( sql.matches( ".* PLTF .*" ) ){
				pltf += "\t";
				pltf += resultset.getString( "PLTF" );
				pltf += "\t";
				showlist.add ( pltf );
				if( k < 1 ){
					System.out.print( "プラットフォーム\t" );
				}
			}
			if( sql.matches( ".* CODI .*" ) ){
				codi += resultset.getString( "CODI" );
				showlist.add( codi );
				if( k < 1 ){
					System.out.print( "状態\t" );
				}
			}
			if( sql.matches( ".* STOCK .*" ) ){
				stock = resultset.getInt( "STOCK" );
				strstock += stock;
				strstock += "　";
				showlist.add( strstock );
				if( k < 1 ){
					System.out.print( "在庫\t" );
				}
			}
			
			if( k < 1 ){
				System.out.println();
			}
			k++;
			//System.out.println( showlist.get(2) );
			//showistの中身を表示
			for( int i = 0 ; i < showlist.size() ; i++ ){
				System.out.print( showlist.get(i) + "\t" );
				
				if( i == ( showlist.size() - 1 ) ){//リストの最後まで来たら改行
					System.out.println();
				}
			}
			//System.out.println( "　商品ID" + id + "　商品名" + name  +  "　ヨミガナ" +  yomi + "　価格" + price + "　ジャンル" + genre + "　発売日" + rele + "　プラットフォーム" + pltf + "　状態" + codi + "　在庫" + stock );
			//次の行に行くので、showlistを空にします。
			showlist.clear();
		}
	}
	public void showTables( ResultSet resultset ) throws SQLException{
		int i = 1;
		System.out.println("テーブル一覧を表示します");
		while( resultset.next() ){
			System.out.println("チェック");
			String tablename = resultset.getString(1);
			System.out.println( "テーブル"+ i + "：" + tablename );
			i++;
		}
	}
	//resultsetのセッター
	public void setResult( ResultSet resultset ){
		this.resultset = resultset;
	}
	
	public void setMetaData( ResultSetMetaData resultsetmetadata ){
		this.resultsetmetadata = resultsetmetadata;
	}
}
