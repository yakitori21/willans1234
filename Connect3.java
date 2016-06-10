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
//INSERT INTO soft2 VALUES( \"A0021\" , \"���̓`��2\" , \"�c���M�m�f���Z�c�c�[\" , 5000 , \"RPG\" , \"2016-2-8\" , \"Tuu\" , \"�V�i\" , 20 )";
//DELETE FROM soft2 WHERE ID = \"A0021\"
public class Connect3{

	private final String SUPERSQL = "SELECT * FROM soft2";
	//*******�X�V�n�������s���Ƃ��̓e�[�u�����ύX�ɒ���***********
	private String sql = "DELETE FROM soft2 WHERE ID = \"A0021\"";
	private Connection connection;
	private ResultSet resultset;
	private PreparedStatement preparedstatement;
	private ResultSetMetaData resultsetmetadata;
	private Display display;
	
	//�R���X�g���N�^�Q
	public Connect3() {
		display = new Display();
		try{
			Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			//**getConnection�̈���2,3�̃��[�U�[���A�p�X���[�h�͊e���̊��ɍ��킹�Ă�������**
			connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/item" , "root" , "" );
			System.out.println( "MySQL�ɐڑ��ł��܂���." );
		}catch( InstantiationException e ){
			System.out.println( "JDBC�h���C�o�̃��[�h�Ɏ��s���܂���.�@�����FInstantiationException" );
		}catch( IllegalAccessException e ){
			System.out.println( "JDBC�h���C�o�̃��[�h�Ɏ��s���܂���.�@�����FIllegalAccessException" );
		}catch( ClassNotFoundException e ){
			System.out.println( "JDBC�h���C�o�̃��[�h�Ɏ��s���܂���.�@�����FClassNotFoundException" );
		}catch( SQLException e ){
			System.out.println( "MySQL�ɐڑ��ł��܂���ł���." );
		}
	}
	//�C���X�^���X��������SQL�����Z�b�g����ꍇ������̃R���X�g���N�^���g�p
	public Connect3( String sql ){
		this.sql = sql;
	}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/5
	�f�[�^�x�[�X�Ɛڑ����邽�߂̃��\�b�h
	SQL���̓��e�ɂ���ď����𕪊򂷂�B
	�������e��Display�^�̃��\�b�h�𗘗p����B
	------------------------------------------------------------------------
	*/
	public void connectDB(){
		try{
			//JDBC�h���C�o�����[�h���܂�
			//Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			
			
			//preparedstatement���g���܂�
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery();
			
			//SQL����SELECT�����ǂ������f
			if( sql.matches( "SELECT.*" ) ){
				//SQL�����̃A�X�^���X�N�̌����B�Ȃ���΁A�e�J�������������Ă���Ɣ��f
				if( sql.matches( ".*\\*.*" ) ){
					display.showSelectAll( resultset );
				}else{
					display.showSelect( sql , resultset );
				}
			}else if( "SHOW TABLES".equals(sql) ){ //SQL����SHOW TABLES�̏ꍇ
				display.showTables( resultset );
			}
			
		
		}catch( SQLException e ){
			System.out.println( "MySQL�ɐڑ��ł��܂���ł���." );
		}/*finally{
			if( connection != null ){
				try{
					preparedstatement.close();
					connection.close();
				}catch( SQLException e ){
					System.out.println("MySQL���N���[�Y�ł��܂���ł����B");
				}
			}
			
		}
		*/
	}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/7
	�ǉ��E�X�V�nSQL���̎��s�p���\�b�h
	------------------------------------------------------------------------
	*/
	public void updateDB(){
		try{
			System.out.println( sql );
			preparedstatement = connection.prepareStatement(sql);
			preparedstatement.executeUpdate();
			
			if( sql.matches( "INSERT INTO.*" ) ){
				System.out.println( "�f�[�^��ǉ����܂����B" );
			}else if( sql.matches( "UPDATE.*" ) ){
				System.out.println( "�f�[�^���X�V���܂����B" );
			}else if( sql.matches( "DELETE.*" ) ){
				System.out.println( "�f�[�^���폜���܂����B" );
			}
			
		}catch( SQLException e ){
			System.out.println( "�X�V���������s�ł��܂���ł����B" );
		}
	
	}
	
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/7
	ResultSet�^��Ԃ����\�b�h
	------------------------------------------------------------------------
	*/
	public ResultSet getResultSet(){
		try{
			String sql = "SELECT * FROM soft";
			//preparedstatement���g���܂�
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery(sql);
		}catch( SQLException e ){
			System.out.println( "MySQL�ɐڑ��ł��܂���ł���." );
		}
		
		return resultset;
	}
	/*
	----------------------------------------------------------
	@author testakaunto 2016/6/7
	ResultSetMetaData�^��Ԃ����\�b�h
	SQL���͂��炩���߃Z�b�g���Ă����B
	----------------------------------------------------------
	*/
	public ResultSetMetaData getMetaData(){
		try{
			String sql = "SELECT * FROM soft";
			preparedstatement = connection.prepareStatement(sql);
			resultset = preparedstatement.executeQuery(sql);
			//resultsetmetadata�ɑ�����܂�
			resultsetmetadata = resultset.getMetaData();
			//**�J���������擾�ł��Ă��邩�e�X�g�i���Lfor�u���b�N�͍폜�\��j**
			/*
			for( int i = 1 ; i < ( resultsetmetadata.getColumnCount() + 1 ) ; i++ ){
				String columnname = resultsetmetadata.getColumnName(i);
				System.out.println( "�J�������F" + columnname );
			}
			*/
		
		}catch( SQLException e ){
			System.out.println( "MySQL�ɐڑ��ł��܂���ł���." );
		}
		return resultsetmetadata;
}
	/*
	------------------------------------------------------------------------
	@author testakaunto 2016/6/5
	�\�����W���[�����쐬���܂������AconnectDB()����\�����\�b�h��
	�Ăяo������connectDB()���ŃN���[�Y�ł���̂ŁA
	���L���\�b�h�͕s�v��������܂���B
	@editor testakaunto 2016/6/7
	preparedstatement.close()��ǉ�
	ResultSet�^�܂���ResultSetMetaData�^��Ԃ��ꍇ�A���ꂼ���
	���\�b�h���ŃN���[�Y�ł��Ȃ��̂ŁA
	Control�N���X��mainloop��false�������钼�O�ɋL�q����̂Ɏg���Ɨǂ��ł��B
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
				System.out.println("MySQL���N���[�Y�ł��܂���ł����B");
			}
		}
	}
	
	//�R���X�g���N�^��sql���Z�b�g���Ă��ǂ��ł����A�Z�b�^�[���p�ӂ��Ă��܂��B
	public void setSql( String sql ){
		this.sql = sql;
	}
	
	//sql�̃Q�b�^�[�ł��B
	public String getSql(){
		return this.sql;
	}
	
	
}