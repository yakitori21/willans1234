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
@editer yakitori21	2016/6/4
@editor testakaunto 2016/6/7
@editer yakitori21	2016/6/9
@editor testakaunto 2016/6/9
*/

//@editor testakaunto 2016/6/9
//ResultSetMetaData resultsetmetadata -> �ϐ���resultsetmetadata�ɕύX
public class Control{
	/* �t�B�[���h */
	private ResultSetMetaData resultsetmetadata;	// �f�[�^�x�[�X�̃J������������
	private ItemManageMain main;	// �I���t���O���䂷��C���X�^���X
	private BufferedReader reader;	// �W�����͗p
	private int windowNo;			// �\�������ʔԍ�
	private Connect3 connect;		// �ʐM���W���[��

	/*
	------------------------------------------------------------
	@editor testakaunto 2016/6/9
	search()�̒ǉ��ɂ��A�e���\�b�h��throws SQLException��ǉ�
	------------------------------------------------------------
	*/


	//-----------------------------------------------------------------------
	// �R���X�g���N�^
	//
	//@author yakitori21 2016/6/4
	// ����	m_ins :	���C�����[�v�̏I���t���O�𐧌䂷�邽�߂̃C���X�^���X
	// 		c_ins : �f�[�^�x�[�X�����擾����ׂ̒ʐM�N���X�̃C���X�^���X
	//@editor testakaunto 2016/6/9
	//-----------------------------------------------------------------------
	public Control(ItemManageMain m_ins/*,Connect c_ins*/){
		// �W�����̓I�u�W�F�N�g���쐬
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.windowNo = TOPMENU; // �g�b�v���j���[�̔ԍ���ݒ�B

		// �C���X�^���X��o�^
		this.main = m_ins;		// ���C���N���X
		this.connect = new Connect3();	// �f�[�^�x�[�X�ʐM�N���X
		
		// �C���X�^���X������
		this.resultsetmetadata = null;		// ResultSetMetaData��������
	}
	
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
	
	//-------------------------------------------------------
	// ��ʔԍ��ɉ����ĕ\���𕪂���
	// ���C�����[�v�����̃��\�b�h���Ăяo��
	// ��ʂ��X�V����
	//
	//@author yakitori21 2016/6/4
	//@editor testakaunto 2016/6/10
	//case SERACH��search()��SQL�����󂯂�String sql��
	//connectDB()�ATOPMENU�ɖ߂鏈����ǉ�
	//-------------------------------------------------------
	public void changeWindow() throws SQLException , IOException {
		// ��ʔԍ������Ƃɉ�ʂ��Ăяo���B
		switch(this.windowNo){
			case TOPMENU:	// �g�b�v���j���[ 
				this.topMenu();	
				break;	
			case TABLE_SELECT:	// �e�[�u���Z���N�g���
				this.selectTable();
				break;
			case ADD_DATA:	// �f�[�^�ǉ����
				System.out.println( "*****�e�[�u�������ύX����Ă��邩���ӂ��Ă��������I�I*****" );
				this.addData();
				break;
			case SERACH:
				String sql = this.search();
				connect.setSql( sql );
				connect.connectDB();
				
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
	//case ALL_DRAW ��Connect2���Ăяo��
	//sql���Z�b�g����R�[�h��ǉ�
	//---------------------------------
	public void topMenu() {
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
							System.out.println(sql);
							this.connect.setSql(sql);	// �ʐM�N���X��SQL�����Z�b�g
							this.connect.connectDB();	// �f�[�^�x�[�X��SQL���𑗐M
							break;
						case ADD:	// �ǉ��@�\
							this.windowNo = ADD_DATA;	// ��ʕύX
							break;
						case SERACH: // ���o�E�����@�\
							this.windowNo = SERACH;		// ��ʕύX
							break;
						default:	// ���O�ɒe���Ă邯�ǈꉞ
							break;
					}
					break;
				}else{
					System.out.println("���͒l������������܂���B0-3�̐��l����͂��Ă�������");
				}
			}catch(NumberFormatException e){
				System.out.println(e);
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
	//@editer yakitori21 2016/6/9
	//-------------------------------------------------------------
	public void addData() throws SQLException , IOException {
		String sql = "INSERT INTO soft2 VALUES(";
		System.out.println("Item�e�[�u���Ƀf�[�^��ǉ����܂��B");
		System.out.println("");

		resultsetmetadata = connect.getMetaData();	// �J���������擾
		
		// �J���������擾
		label:	// ���x��break
		for (int i=1; i<=this.resultsetmetadata.getColumnCount(); i++){
			String columnName = this.resultsetmetadata.getColumnName(i);
			System.out.println("�J������:"+relabelString(columnName)+"�ɒǉ�����f�[�^����͂��Ă�������");
			
			// �K�؂ȃf�[�^����͂��Ȃ�������J��Ԃ�
			while(true){
				String line = reader.readLine();
				if(line == null){	// ���[�U�[�����͂��L�����Z��
					System.out.println("���͂��L�����Z������܂����B�g�b�v���j���[�ֈړ����܂�");
					windowNo = TOPMENU;
					break label;	// ���x��break��for�����̂�E�o�B
				}
				if(this.chkConsistency(columnName,line)){	// �������͂����������H
					if(i!=resultsetmetadata.getColumnCount()){	// �Ō�̃J�������͂łȂ��Ȃ�
						sql = "\'"+line+"\' ,";
					}else{
						sql = "\'"+line+"\'";
					}
					break;	// ���͂̈ꏄ�I���B
				}
			}
		}
		sql = sql+");";	// SQL���̏I�[��ݒ� 
		System.out.println(sql);
		/*this.connect.setSql(sql);
		this.connect.connectDB();*/
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
		//ResultSet resultset = connect.getResultSet();
		resultsetmetadata = connect.getMetaData();
		//�\���p�̃J������������columnlist�Ǝ��ۂ̃J������������truecolumnlist���쐬
		List<String> columnlist = new ArrayList<String>();
		List<String> truecolumnlist = new ArrayList<String>();
		
		//columnlist�ɕ\���p�̃J�����������܂��B
		columnlist.add( "���iID" );
		columnlist.add( "���i��" );
		columnlist.add( "���~�K�i" );
		columnlist.add( "���i" );
		columnlist.add( "�W������" );
		columnlist.add( "������" );
		columnlist.add( "�v���b�g�t�H�[��" );
		columnlist.add( "���" );
		columnlist.add( "�݌�" );
		
		//truecolumnlist�Ɏ��ۂ̃J�����������܂��B
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		//�I����I������ƃ��[�v�𔲂��܂��B
		while( searchloop ){
			if( k < 1 ){//���[�v��1��ڂ��ǂ����ŕ\����ς��܂�
				System.out.println( "���o����f�[�^�̍��ڂ�I�сA��������͂��Ă��������B" );
			}else{
				System.out.println("���ɒ��o����f�[�^���ڂ�����΁A��������͂��Ă��������B");
				System.out.println( selectcolumn );//���ɑI�������f�[�^��\�����܂��B
			}
			for( int i = 0 ; i < columnlist.size() ; i++ ){//�\���p�J�������𐔎���t���ĕ\�����܂�
				System.out.println( "" + i + "�F" + columnlist.get(i));
			}
			//�I���͍Ō�̃J�������ɕ\�����܂��B
			System.out.println( "" + columnlist.size() + "�F" + "���I���I����" );
			try{
				String str = reader.readLine();
				int num = Integer.parseInt( str );
				//���̓`�F�b�N�����Ȃ���sql�ɃJ��������ǉ����܂��B
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
					//columnlist�Atruecolumnlist�͗v�f�ԍ����Ή����Ă���̂ŁA�����Ƃ������ԍ���remove���܂��B
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
		windowNo = TOPMENU;
		//�Ō��sql���̕�����t�������Asql��Ԃ��܂��B
		sql += "FROM soft";
		return sql;
	}

	//--------------------------------------------
	// �f�[�^�������
	// ��������J���������[�U�[�Ɏw�肵�Ă��炤
	//
	//@Author yakitori21 2016/6/9
	//--------------------------------------------
	/*public void SearchData(){
		String line;
		ArrayList<String> list = new ArrayList<String>();
		this.resultsetmetadata = connect.getMetaData();	// �J���������擾
		
		// �J�����f�[�^��o�^
		for(int i=1;i<=this.getColumnCount();i++){
			String columnName = this.resultsetmetadata.getColumnName(i);
			list.add(columnName);
		}
		
		// ���[�U�[����
		while((line = reader.readLine())!=null){
			

			// ���͈ē�
			System.out.println("�ȉ����猟������ԍ���I�ѓ��͂��Ă��������B");
			for(int i=0;i<list.size();i++){
				System.out.print(i+":"+list.get()+"\t");
			}
		}
	}*/
	
	//--------------------------------------
	// ���͒l�̐��������m�F����
	//@author yakitori21 2016/6/4
	//@editer yakitori21 2016/6/9
	// ����		column : ��������J������
	// 			st	   : ��������Ώە�����
	//--------------------------------------
	public boolean chkConsistency(String column,String st){
		boolean chk = false;
		if(column.equals("ID")){		// ���iID
			chk = st.matches("A[0-9]{4}");	// A��(0�`9)��4�����\��
		}else if(column.equals("NAME")){		// ���i��
			chk = st.matches(".{1,30}");	// 1�����ȏ�30�����ȉ�
		}else if(column.equals("YOMI")){		// ���i�����~
			chk = st.matches("[�@-���-�]{1,30}"); // �S�p�J�^�J�i�܂��͔��p��1�����ȏ�30�����ȉ�
		}else if(column.equals("PRICE")){	// ���i
			int input = ChgInt(st);
			chk = (input <= 50000);	// 50000�~�ȏ�͓o�^�s��
		}else if(column.equals("GENRE")){
			chk = st.matches(".{1,10}");
		}else if(column.equals("RELE")){	// time���\�b�h�Ƀf�[�^�����ăt�H�[�}�b�g���Ԉ���Ă�����͂���(��)
			chk = st.matches("20[0-9]{2}/[1]*[0-9]/[1-3]*[0-9]");
		}else if(column.equals("PLTF")){
			chk = st.matches(".{1,10}");
		}else if(column.equals("CODI")){
			chk = st.matches(".{1,10}");
		}else if(column.equals("STOCK")){
			int input = ChgInt(st);
			chk = (input <= 100);	// �݌�100�ȏ�͓o�^�s��
		}
		return chk;
	}

	//--------------------------------------------------
	// �J��������\���p�̐V�������O�ɕς���
	// 
	//@author yakitori21 2016/6/9
	//����	st	:	�J������
	//�߂�l	:	�V�������O
	//---------------------------------------------------
	public String relabelString( String st ){
		String res_st = "";
		if(st.equals("ID")){
			res_st = "���iID";
		}else if(st.equals("NAME")){
			res_st = "���i��";
		}else if(st.equals("YOMI")){
			res_st = "���~�K�i";
		}else if(st.equals("PRICE")){
			res_st = "���i";
		}else if(st.equals("GENRE")){
			res_st = "�W������";
		}else if(st.equals("RELE")){
			res_st = "�����[�X";
		}else if(st.equals("PLTF")){
			res_st = "�v���b�g�t�H�[��";
		}else if(st.equals("CODI")){
			res_st = "���";
		}else if(st.equals("STOCK")){
			res_st = "�݌�";
		}
		return res_st;
	}

	//-----------------------------------
	// �f�[�^�x�[�X�̎��㏈��
	// �f�[�^�x�[�X�̊J���������Ăяo��
	//
	//@author yakitori21 2016/6/9
	//------------------------------------
	public void closeDatabase(){
		connect.connectClose();	// �f�[�^�x�[�X�ؒf
	}

	//------------------------------------
	// ������^�̐����^�ɕϊ�
	// ��O�L�q�̏ȗ�
	//
	//@author yaktiori212016/6/9
	//����	st	:	�ϊ����镶����
	//�߂�l	:	�ϊ���̐���
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
	�f�[�^���X�V����SQL�����쐬���郁�\�b�h
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
		
		//columnlist�ɕ\���p�̃J�����������܂��B
		columnlist.add( "���iID" );
		columnlist.add( "���i��" );
		columnlist.add( "���~�K�i" );
		columnlist.add( "���i" );
		columnlist.add( "�W������" );
		columnlist.add( "������" );
		columnlist.add( "�v���b�g�t�H�[��" );
		columnlist.add( "���" );
		columnlist.add( "�݌�" );
		
		//truecolumnlist�Ɏ��ۂ̃J�����������܂��B
		for( int l = 1 ; l < resultsetmetadata.getColumnCount() + 1 ; l++ ){
			truecolumnlist.add( resultsetmetadata.getColumnName(l) );
		}
		
		for( int i = 0 ; i < columnlist.size() ; i++ ){//�\���p�J�������𐔎���t���ĕ\�����܂�
				System.out.println( "" + i + "�F" + columnlist.get(i));
			}
		
		
		while( idloop ){
			try{
				System.out.println( "�X�V�������f�[�^�̏��iID����͂��Ă��������B" );
				id = reader.readLine();
				idloop = chkConsistency( "ID" , id );
				//id���͂����������`�F�b�N
				if( idloop != true ){
					System.out.println( "���͂����l������������܂���B" );
					continue;
				}
				connect.setSql("SELECT ID FROM soft");
				resultset = connect.getResultSet();
				//�w�肵��ID���e�[�u����ɑ��݂��邩�`�F�b�N
				while( resultset.next() ){
					String iddata = resultset.getString( "ID" );
					if( !id.equals( iddata ) ){
						System.out.println( "���݂��Ȃ��f�[�^�ł��B" );
						break;
					}
				
				}
			}catch( IOException e ){
				System.out.println( "���̓G���[���������܂���" );
				continue;
			}
		}
		connect.setSql("SELECT * FROM soft2");
		System.out.println( "���ɍX�V����f�[�^���ڂ�I�����A��������͂��Ă��������B" );
		while( columnloop ){
			try{
				for( int i = 0 ; i < columnlist.size() ; i++ ){//�\���p�J�������𐔎���t���ĕ\�����܂�
					System.out.println( "" + i + "�F" + columnlist.get(i) );
				}
				String strnum = reader.readLine();
				int num = Integer.parseInt( strnum );
				column = " " + truecolumnlist.get(i) + " ";
			}catch( IOException e ){
				System.out.println( "���̓G���[���������܂���" );
				continue;
			}catch( NumberFormatException e ){
				System.out.println( "���l�ȊO�����͂���܂����B���͂���蒼���Ă��������B" );
				continue;
			}
		}
		System.out.println( "�Ō�Ƀf�[�^�̒l����͂��Ă��������B" );
		
		sql += column + "=" data + "WHERE ID =" + id;
	}
	
}

