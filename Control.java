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
	// �R���X�g���N�^
	//
	//@author yakitori21 2016/6/4
	// ����	m_ins :	���C�����[�v�̏I���t���O�𐧌䂷�邽�߂̃C���X�^���X
	// 		c_ins : �f�[�^�x�[�X�����擾����ׂ̒ʐM�N���X�̃C���X�^���X
	//-----------------------------------------------------------------------
	public Control(ItemManageMain m_ins/*,Connect c_ins*/){
		// �W�����̓I�u�W�F�N�gR���쐬
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.windowNo = TOPMENU; // �g�b�v���j���[�̔ԍ���ݒ�B

		// �C���X�^���X��o�^
		this.main = m_ins;		// ���C���N���X
		this.connect = new Connect3();	// �f�[�^�x�[�X�ʐM�N���X
		
		// �f�[�^�x�[�X�����擾
		//this.rs = con.get���\�b�h(��)
		//this.rsmd = con.get���\�b�h(��)
	}
	
	/* �t�B�[���h */

	//private ResultSet rs;			// �f�[�^�x�[�X�̓�����������
	//private ResultSetMetaData rsmd;	// �f�[�^�x�[�X�̃J������������
	//private Connect con;			// �f�[�^�x�[�X�����擾����ׂ̃C���X�^���X
	private ItemManageMain main;	// �I���t���O���䂷��C���X�^���X
	private BufferedReader reader;	// �W�����͗p
	private int windowNo;			// �\�������ʔԍ�
	private Connect3 connect;
	/* �萔 */

	// ��ʑ���
	private static final int TOPMENU = 0;		// �g�b�v���j��:0
	private static final int TABLE_SELECT = 1;	// �e�[�u���I�����:1
	private static final int ADD_DATA = 2;		// �f�[�^�ǉ����:2

	// �@�\
	private static final int END = 0;
	private static final int ALL_DRAW = 1;
	private static final int ADD = 2;
	private static final int SERACH = 3;
	
	/* ���\�b�h */
	
	//----------------------------------------
	// ��ʔԍ��ɉ����ĕ\���𕪂���
	// ���C�����[�v�����̃��\�b�h���Ăяo��
	// ��ʂ��X�V����
	//
	//@author yakitori21 2016/6/4
	//----------------------------------------
	public void changeWinodow() throws SQLException {
		// ��ʔԍ������Ƃɉ�ʂ��Ăяo���B
		switch(this.windowNo){
			case TOPMENU:	// �g�b�v���j���[ 
				this.topMenu();	
				break;	
			case TABLE_SELECT:	// �e�[�u���Z���N�g���
				this.selectTable();
				break;
			case ADD_DATA:	// �f�[�^�ǉ����
				this.addData();
				break;
			default:
				System.out.println("Error:��ʑJ�ڈُ�ɂ��V�X�e�����I�����܂��B");
				this.main.setLoopEnd();	// ���C�����[�v�I��
				break;
		}
	}

	//---------------------------------
	// �g�b�v���j���ł̑���
	// 
	//@author yakitori21 2016/6/4
	//@editor testakaunto 2016/6/7
	//case ALL_DRAW ��Connect3���Ăяo��
	//sql���Z�b�g����R�[�h��ǉ�
	//---------------------------------
	public void topMenu() throws SQLException {
		String line = "";	// �W�����͂��󂯎��ϐ�
		System.out.println();
		System.out.println( "���j���[��I��ł��������B" );
		System.out.println();
		System.out.println( "0�F���i�Ǘ��V�X�e���̏I��" );
		System.out.println( "1�F�e�[�u���̏��i��S�ĕ\������" );
		System.out.println( "2�F�e�[�u���Ƀf�[�^��ǉ�����" );
		System.out.println( "3�F�������w�肵�ăe�[�u�����̃f�[�^��\������" );
		System.out.println();
		
		// ���͂����������܂�
		while(true){
			try{
				if(( line = this.reader.readLine())==null ){	// ����
					System.out.println("���̓L�����Z������܂����B�v���O�������I�����܂�");
					this.main.setLoopEnd();
					break;
				}
				

				// ���͐���[0-3]
				if(line.matches("[0-3]")){
					int input = Integer.parseInt(line);	// ���l�^�ϊ�
					// ���͒l�ɉ����ď����𕪊�
					switch(input){
						case END:	// �I��
							this.main.setLoopEnd();  // ���C�����[�v��E�o����t���O����
							break;
						case ALL_DRAW: 	// �e�[�u���̏��i��S�ĕ\������
							String sql = "SELECT * FROM soft;";
							System.out.println( sql );
							this.connect.setSql( sql );	// �ʐM�N���X��SQL�����Z�b�g
							this.connect.connectDB();	// �f�[�^�x�[�X��SQL���𑗐M
							break;
						case ADD:	// �ǉ��@�\
							//this.windowNo = ADD_DATA;	// ��ʕύX
							break;
						case SERACH: // ���o�E�����@�\-������(��)
							sql = search();
							System.out.println( sql );
							this.connect.setSql( sql );
							this.connect.connectDB();
							break;
						default:	// ���O�ɒe���Ă邯�ǈꉞ
							break;
					}
					break;
				}else{
					System.out.println("���͒l������������܂���B0-3�̐��l����͂��Ă�������");
				}
			}catch(NumberFormatException e){
				//System.out.println(e);
			}catch(IOException e){
				System.out.println(e);
			}
		}
	}

	//-------------------------------
	// �e�[�u���I�����-������(��)
	// 
	//@author yakitori21 2016/6/4
	//-------------------------------
	public void selectTable(){
		String line = "";
		System.out.println("�e�[�u����ʂ�\�����܂����B");

		try{
			while(true){
				line = reader.readLine();
				if(line == null){	// ���[�U�[�����͂��L�����Z��
					System.out.println("���͂��L�����Z������܂����B�g�b�v���j���[�ֈړ����܂�");
					windowNo = TOPMENU;
				}
				System.out.println("���͂��m�F���܂����B�g�b�v���j���[�ɖ߂�܂�");
				windowNo = TOPMENU;
				break;
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	//-------------------------------------------------------------
	// �f�[�^�ǉ����
	// ���[�U�[�����͂��s���A���̓f�[�^���f�[�^�x�[�X�ɓo�^����B
	// �����œ��̓f�[�^���쐬��Connect�I�u�W�F�N�g�ɑ��M����
	//@author yakitori21 2016/6/4
	//-------------------------------------------------------------
	public void addData(){
		String line = "";			// �W������
		String columnName = "";		// �J������
		String sql = "INSERT INTO soft VALUES(";
		System.out.println("Item�e�[�u���Ƀf�[�^��ǉ����܂��B");
		System.out.println("");

		try{
			while(true){
				line = reader.readLine();
				if(line == null){	// ���[�U�[�����͂��L�����Z��
					System.out.println("���͂��L�����Z������܂����B�g�b�v���j���[�ֈړ����܂�");
					windowNo = TOPMENU;
				}
				System.out.println("���͂��m�F���܂����B�g�b�v���j���[�ɖ߂�܂�");
				windowNo = TOPMENU;
				break;
			}
		}catch(IOException e){
			System.out.println(e);
		}
		// �J���������擾
		/*label:	// ���x��break
		for (int i = 1; i <= this.rsmd.getColumnCount(); i++) {
			columnName = this.rsmd.getColumnName(i);
   			System.out.println("�J������:"+columnName+"�ɒǉ�����f�[�^����͂��Ă�������");
			
			// �K�؂ȃf�[�^����͂��Ȃ�������J��Ԃ�
			while(true){
				line = reader.readLine();
				if(line == null){	// ���[�U�[�����͂��L�����Z��
					System.out.println("���͂��L�����Z������܂����B�g�b�v���j���[�ֈړ����܂�");
					windowNo = TOPMENU;
					break label;	// ���x��break��for�����̂�E�o�B
				}
				if(this.chkConsistency(columnName,line)){	// �������͂����������H
					if(i!=rsmd.getColumnCount()){	// �Ō�̃J�������͂łȂ��Ȃ�
						sql = "\'"+line+"\' ,";
					}else{
						sql = "\'"+line+"\'";
					}
					break;	// ���͂̈ꏄ�I���B
				}
			}
		}
		sql = sql+");";	// SQL���̏I�[��ݒ� 
		*/
	}
	
	//--------------------------------------
	// ���͒l�̐��������m�F����-������(��)
	//@author yakitori21 2016/6/4
	// ����		colum : ��������J������
	// 			st	  : ��������Ώە�����
	//--------------------------------------
	public boolean chkConsistency(String colum,String st){
		boolean chk = false;
		if(colum == "ID"){		// ���iID
			chk = st.matches("A[0-9]{4}");	// A��(0�`9)��4�����\��
		}else if(colum == "NAME"){		// ���i��
			chk = st.matches(".{1,30}");	// 1�����ȏ�30�����ȉ�
		}else if(colum == "YOMI"){		// ���i�����~
			chk = st.matches("[�@-���-�]{1,30}"); // �S�p�J�^�J�i�܂��͔��p��1�����ȏ�30�����ȉ�
		}else if(colum == "PRICE"){	// ���i
			try{
				int input = Integer.parseInt(st);
				chk = (st.matches("[1-5]*[0-9]{1,4}") && (input <= 50000) );	// 50000�~�ȏ�͓o�^�s��
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
	�f�[�^�𒊏o���ĕ\������SQL��
	���쐬���郁�\�b�h���쐬���܂����B
	--------------------------------------------------
	*/
	
	public String search() throws SQLException {
		int k = 0;//�J�E���^�ϐ�
		
		boolean searchloop = true;
		String sql = "SELECT";
		String selectcolumn = "�I���������ځF";
		ResultSet resultset = connect.getResultSet();
		ResultSetMetaData resultsetmetadata = connect.getMetaData();
		List<String> columnlist = new ArrayList<String>();
		List<String> truecolumnlist = new ArrayList<String>();
		
		columnlist.add( "���iID" );
		columnlist.add( "���i��" );
		columnlist.add( "���~�K�i" );
		columnlist.add( "���i" );
		columnlist.add( "�W������" );
		columnlist.add( "������" );
		columnlist.add( "�v���b�g�t�H�[��" );
		columnlist.add( "���" );
		columnlist.add( "�݌�" );
		
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		
		while( searchloop ){
			if( k < 1 ){
				System.out.println( "���o����f�[�^�̍��ڂ�I�сA��������͂��Ă��������B" );
			}else{
				System.out.println("���ɒ��o����f�[�^���ڂ�����΁A��������͂��Ă��������B");
				System.out.println( selectcolumn );
			}
			for( int i = 0 ; i < columnlist.size() ; i++ ){
				System.out.println( "" + i + "�F" + columnlist.get(i));
			}
			System.out.println( "" + columnlist.size() + "�F" + "���I����" );
			try{
				String str = reader.readLine();
				int num = Integer.parseInt( str );
				if( num < 0 || num > columnlist.size() ){
					System.out.println( "���͂��ꂽ���l������������܂���B���͂���蒼���Ă��������B" );
					continue;
				}else if( num < columnlist.size() ){
					if( k > 0 ){
						sql += ",";
						selectcolumn += "�A";
						
					}
					sql += " " + truecolumnlist.get( num ) + " ";
					selectcolumn += columnlist.get( num );
					columnlist.remove( num );
					truecolumnlist.remove( num );
				}else if( num == columnlist.size() ){
					System.out.println( "���������̒ǉ����I�����A���ʂ�\�����܂��B" );
					searchloop = false;
					break;
				}
				
				
			}catch( NumberFormatException e ){
				System.out.println( "���l�ȊO�����͂���܂����B���͂���蒼���Ă��������B" );
				continue;
			}catch( IOException e ){
				System.out.println( "���̓G���[���������܂����B���͂���蒼���Ă��������B" );
				continue;
			}
			k++;
		}
		
		sql += "FROM soft";
		return sql;
	}
}