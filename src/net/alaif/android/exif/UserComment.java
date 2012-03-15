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

	/* プライベートメンバ変数 */
	private String m_file_path;
	
	/***
	 * コンストラクタ
	 * @param jpgfilepath ： 操作対象のJPGファイルのパス
	 */
	public UserComment(String jpgfilepath)
	{
		m_file_path = jpgfilepath;
	}
	
	/***
	 * UserCommentを取得（UTF-8）
	 * @return UserComment
	 */
	public String getUserComment()
	{
		return getUserComment("UTF-8");
	}
	
	/***
	 * UserCommentを取得
	 * @param encodename： 文字エンコードを指定
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
	 * UserCommentを設定（UTF-8）
	 * @param comment UserComment
	 * @return true:成功, false:失敗
	 */
	public boolean putUserComment(String comment)
	{
		return putUserComment(comment, "UTF-8");
	}
	
	/***
	 * UserCommentを設定
	 * @param comment UserComment
	 * @param encodename 文字エンコーディング名
	 * @return true:成功, false: 失敗
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
