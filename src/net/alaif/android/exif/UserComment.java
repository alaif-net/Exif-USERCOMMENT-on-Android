package net.alaif.android.exif;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UserComment {
	static
	{
		System.loadLibrary("exifcomment");
		System.loadLibrary("exif_usercomment_wrapper");
	}
	
	private native byte[] getExifUserComment(byte[] srcfilepath);
	private native int putExifUserComment(byte[] srcfilepath, byte[] comment, byte[] destfilepath);

	/* �v���C�x�[�g�����o�ϐ� */
	private String m_file_path;
	
	/***
	 * �R���X�g���N�^
	 * @param jpgfilepath �F ����Ώۂ�JPG�t�@�C���̃p�X
	 */
	public UserComment(String jpgfilepath)
	{
		m_file_path = jpgfilepath;
	}
	
	/***
	 * UserComment���擾�iUTF-8�j
	 * @return UserComment
	 */
	public String getUserComment()
	{
		return getUserComment("UTF-8");
	}
	
	/***
	 * UserComment���擾
	 * @param encodename�F �����G���R�[�h���w��
	 * @return UserComment
	 */
	public String getUserComment(String encodename)
	{
		byte[] ret;
		byte[] srcfile = m_file_path.getBytes();
		
		ret = getExifUserComment(srcfile);
		if (ret == null) {
			return null;
		}
		
		try {
			return new String(ret, encodename);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/***
	 * UserComment��ݒ�iUTF-8�j
	 * @param comment UserComment
	 * @return true:����, false:���s
	 */
	public boolean putUserComment(String comment)
	{
		return putUserComment(comment, "UTF-8");
	}
	
	/***
	 * UserComment��ݒ�
	 * @param comment UserComment
	 * @param encodename �����G���R�[�f�B���O��
	 * @return true:����, false: ���s
	 */
	public boolean putUserComment(String comment, String encodename)
	{
		try {
			byte[] comment_array = comment.getBytes(encodename);
			byte[] file_path = m_file_path.getBytes();
						
			File tmpfile = File.createTempFile("exifpatch", ".jpg");
			String tmpfile_name = tmpfile.getAbsolutePath();
			byte[] tmpfile_path = tmpfile_name.getBytes();
			
			int ret = putExifUserComment(file_path, comment_array, tmpfile_path);
			return (ret == 0);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
