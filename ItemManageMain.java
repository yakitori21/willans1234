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
//�e���\�b�h��throws SQLException , IOException��ǉ�
public class ItemManageMain {
	/* �t�B�[���h */
	private Control ctrl; 	// ����I�u�W�F�N�g
	private boolean loopflg;	// �I���t���O

	//------------------------------------------------
	// �R���X�g���N�^
	//
	// �t�B�[���h�̏������A�I�u�W�F�N�g�̐������s��
	//@author yakitori21 2016/6/5
	//------------------------------------------------
	public ItemManageMain() throws SQLException {
		// �t�B�[���h�̏�����
		loopflg = true;

		System.out.println("main�I�u�W�F�N�g�쐬");
	
		// �I�u�W�F�N�g�̐���
		ctrl = new Control(this); // ����
	}
	
	/* ���\�b�h */

	// ���C������
	public static void main( String[] args ) throws SQLException , IOException{
		// ���g�̃I�u�W�F�N�g���쐬
		// (���g�̎Q�ƒl�𑼃I�u�W�F�N�g�ɓn���Ȃ���)
		ItemManageMain mainIns = new ItemManageMain();

		mainIns.mainLoop();	// ���[�v�������Ă�
		

		System.exit(0);
	}
	
	//------------------------------------------------
	// ���[�v���\�b�h
	//
	// �I������Ȃ����胋�[�v���J��Ԃ��d�g��
	//@author yakitori21 2016/6/5
	//------------------------------------------------
	public void mainLoop() throws SQLException , IOException {
		System.out.println( "���i�Ǘ��V�X�e���ւ悤�����I" );
		
		// ���C�����[�v
		while( this.loopflg ){
			ctrl.changeWindow();	// ��ʐؑփ��\�b�h���Ăяo��
		}
		ctrl.closeDatabase();
		System.out.println("");
		System.out.println("--------------------------");
		System.out.println("�v���O�������I�����܂��B");
	}

	// ���C�����[�v�̒E�o
	public void setLoopEnd(){
		this.loopflg = false;
	}
}
