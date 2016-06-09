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
Connect�N���X�ɕ\���̃R�[�h���璷�ɂȂ������߁A
Display�N���X�ɕ\���̏����𕪊�
�ESELECT * ... 
�ESELECT [�J������1, �J������2, ...] ...
�ESHOW TABLES
��LSQL�����ꂼ��Ń��\�b�h���Ƃɏ����𕪂��܂����B
----------------------------------------------------
*/

public class Display{
	private ResultSet resultset;
	private ResultSetMetaData resultsetmetadata;
	private List<String> showlist;
	
	//�R���X�g���N�^�Q
	public Display(){
	
	}
	public Display( ResultSet resultset ){
		this.resultset = resultset;
		
	}
	
	public void showSelectAll( ResultSet resultset ) throws SQLException{
		//�\������f�[�^�����Ă���List�^��showlist��錾
		List<String> showlist = new ArrayList<String>();
		
		//���i���A���~�K�i��15�������̘g��p�ӂ��邽�߁A�ő啶����MAX_LENGTH��15�ŏ��������Đ錾
		final int MAX_LENGTH = 15;
		System.out.println( "���iID\t" + "    ���i��\t\t\t" + "    ���~�K�i\t\t\t" + "���i\t" + "�W������\t\t\t" + "�@������\t" + "�v���b�g�t�H�[��\t" + "���\t" + "�݌�\t" );
		while( resultset.next() ){
			//�J������������String�^�̕ϐ��������Aint�^�ϐ���getInt���󂯂邽�߂ɗp��
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
			
			//�ȉ��A�f�[�^��\���p�ɐ��`
			if( name.length() < MAX_LENGTH ){
				if( name.length() < 4 ){
					name += "\t\t";
				}else if( name.length() < 8 ){
					name += "\t";
				}else if( "�O�����h�J�[�g2".equals( name ) ){//�O�����h�J�[�g2�̂ݕ\��������Ȃ��̂ŏ�����t�������܂����B
					name += "\t\t";
				}
				for( int i = 0 ; i < ( MAX_LENGTH - name.length() ) ; i++ ){
					name += "�@";
				}
			}
			if( yomi.length() < MAX_LENGTH ){
				if( yomi.length() < 4 ){
					yomi += "\t\t";
				}else if( yomi.length() < 8 ){
					yomi += "\t";
				}
				for( int i = 0 ; i < ( MAX_LENGTH - yomi.length() ) ; i++ ){
					yomi += "�@";
				}
			}
			if( genre.length() < 4 ){
				genre += "\t\t";
			}else if( genre.length() < 8 ){
				genre += "\t";
			}
			pltf += "\t";
			//���X�g�ɐ��`�����f�[�^��ǉ�
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
			//���̍s�Ɉڂ邽��showlist���N���A
			showlist.clear();
		}
	}
	/*
	----------------------------------------------------------------
	@editor testakaunto 2016/6/7
	if���Ƀ��~�K�i���Ȃ������̂Œǉ�
	Connect�^���̃C���X�^���X�ϐ�sql��resultset
	sql�̓J���������܂܂�Ă��邩�̔��ʂɗ��p
	�w�肷��J�������̕������
	�Ⴄ�����񂪌���(example."NAME"��"MAKERNAME"�Ȃ�)����\���ւ�
	�΍�Ƃ��āA��������J�������̑O��ɔ��p�󔒂����Č�������
	����āASQL���������ł����p�󔒂����Đ������Ă��炤�悤�ɂ���
	----------------------------------------------------------------
	*/
	public void showSelect( String sql , ResultSet resultset ) throws SQLException{			
		//�\�����邽�߂̃��X�g���C���X�^���X��
		showlist = new ArrayList<String> ();
		int k = 0;
		while( resultset.next() ){
			//�J������������String�^�̕ϐ��������Aint�^�ϐ���getInt���󂯂邽�߂ɗp��
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
			
			//���i���A���~�K�i��15�������̘g��p�ӂ��邽�߁A�ő啶����MAX_LENGTH��15�ŏ��������Đ錾
			final int MAX_LENGTH = 15;
			
			System.out.println();
			
			//SQL�����̃J���������������A�Y���J�����������showlist�ɒǉ�
			//int�^��String�^�ɕϊ�
			if( sql.matches( ".* ID .*" ) ){
				id += resultset.getString( "ID" );
				showlist.add( id );
				if( k < 1 ){
					System.out.print( "���iID\t" );
				}
			}
			if( sql.matches( ".* NAME .*" ) ){
				name += resultset.getString( "NAME" );
				//��������MAX_LENGTH�Ɣ�r���A15�����ȉ��ł���΁A�������ɉ�����TAB���s��
				if( name.length() < MAX_LENGTH ){
					if( name.length() < 4 ){
						name += "\t\t";
					}else if( name.length() < 8 ){
						name += "\t";
					}else if( "�O�����h�J�[�g2".equals( name ) ){//�O�����h�J�[�g2�̂ݕ\��������Ȃ��̂ŏ�����t�������܂����B
						name += "\t\t";
					}
					for( int i = 0 ; i < ( MAX_LENGTH - name.length() ) ; i++ ){
						name += "�@";
					}
				}
				showlist.add( name );
				if( k < 1 ){
					System.out.print( "    ���i��\t\t\t" );
				}
			}
			if( sql.matches( ".* YOMI .*" ) ){
				yomi += resultset.getString( "YOMI" );
				//name��if�u���b�N�Ɠ���
				if( yomi.length() < MAX_LENGTH ){
					if( yomi.length() < 4 ){
						yomi += "\t\t";
					}else if( yomi.length() < 8 ){
						yomi += "\t";
					}
					for( int i = 0 ; i < ( MAX_LENGTH - yomi.length() ) ; i++ ){
						yomi += "�@";
					}
				}
				showlist.add( yomi );
				if( k < 1 ){
					System.out.print( "    ���~�K�i\t\t\t" );
				}
			}
			if( sql.matches( ".* PRICE .*" ) ){
				price = resultset.getInt( "PRICE" );
				strprice += String.valueOf( price );
				showlist.add( strprice );
				if( k < 1 ){
					System.out.print( "���i\t" );
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
					System.out.print( "�W������\t\t" );
				}
			}
			if( sql.matches( ".* RELE .*" ) ){
				rele += resultset.getString( "RELE" );
				showlist.add( rele );
				if( k < 1 ){
					System.out.print( "�@������\t" );
				}
			}
			if( sql.matches( ".* PLTF .*" ) ){
				pltf += "\t";
				pltf += resultset.getString( "PLTF" );
				pltf += "\t";
				showlist.add ( pltf );
				if( k < 1 ){
					System.out.print( "�v���b�g�t�H�[��\t" );
				}
			}
			if( sql.matches( ".* CODI .*" ) ){
				codi += resultset.getString( "CODI" );
				showlist.add( codi );
				if( k < 1 ){
					System.out.print( "���\t" );
				}
			}
			if( sql.matches( ".* STOCK .*" ) ){
				stock = resultset.getInt( "STOCK" );
				strstock += stock;
				strstock += "�@";
				showlist.add( strstock );
				if( k < 1 ){
					System.out.print( "�݌�\t" );
				}
			}
			
			if( k < 1 ){
				System.out.println();
			}
			k++;
			//System.out.println( showlist.get(2) );
			//showist�̒��g��\��
			for( int i = 0 ; i < showlist.size() ; i++ ){
				System.out.print( showlist.get(i) + "\t" );
				
				if( i == ( showlist.size() - 1 ) ){//���X�g�̍Ō�܂ŗ�������s
					System.out.println();
				}
			}
			//System.out.println( "�@���iID" + id + "�@���i��" + name  +  "�@���~�K�i" +  yomi + "�@���i" + price + "�@�W������" + genre + "�@������" + rele + "�@�v���b�g�t�H�[��" + pltf + "�@���" + codi + "�@�݌�" + stock );
			//���̍s�ɍs���̂ŁAshowlist����ɂ��܂��B
			showlist.clear();
		}
	}
	public void showTables( ResultSet resultset ) throws SQLException{
		int i = 1;
		System.out.println("�e�[�u���ꗗ��\�����܂�");
		while( resultset.next() ){
			System.out.println("�`�F�b�N");
			String tablename = resultset.getString(1);
			System.out.println( "�e�[�u��"+ i + "�F" + tablename );
			i++;
		}
	}
	//resultset�̃Z�b�^�[
	public void setResult( ResultSet resultset ){
		this.resultset = resultset;
	}
	
	public void setMetaData( ResultSetMetaData resultsetmetadata ){
		this.resultsetmetadata = resultsetmetadata;
	}
}
