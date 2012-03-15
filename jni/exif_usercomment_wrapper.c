#include <string.h>
#include <stdio.h>
#include "net_alaif_android_exif_UserComment.h"

/* libexifcommentのプロトタイプ */
int get_user_comment(char *infilename, char *outcomment, int incommentlen);
int put_user_comment(char *infilename, char *incomment, int incommentlen, char *outfilename);

/*
 * Exif UserComment取得
 JNIEXPORT jstring JNICALL Java_net_alaif_android_exif_UserComment_getComment(JNIEnv *env, jobject obj, jstring filepath)
{
	const char *in_path = (*env)->GetStringUTFChars(env, filepath, NULL);
	char msgbuf[1024];
	
	//
	int ret = get_user_comment(in_path, msgbuf, 1024);
	if (ret == 0) {
		return (*env)->NewStringUTF(env, msgbuf);
	} else {
		return "";
	}
}
*/

JNIEXPORT jbyteArray JNICALL Java_net_alaif_android_exif_UserComment_getExifUserComment(JNIEnv *env, jobject obj, jbyteArray patharray)
{
	jbyte *path = (*env)->GetByteArrayElements(env, patharray, NULL);
	jbyte msg[1024];
	
	jbyteArray msgarray = (*env)->NewByteArray(env, 1);
	
	int ret = get_user_comment((char*)path, (char*)msg, 1024);
	if (ret == 0) {
		msgarray = (*env)->NewByteArray(env, strlen((char*)msg));
		(*env)->ReleaseByteArrayElements(env, msgarray, msg, 0);
	}
	return msgarray;
}

/*
 * Exif UserComment設定

JNIEXPORT jint JNICALL Java_net_alaif_android_exif_UserComment_putComment(JNIEnv *env, jobject obj, jstring infilepath, jstring comment, jstring outfilepath)
{
	const char *in_infile_path  = (*env)->GetStringUTFChars(env, infilepath, NULL);
	const char *in_comment      = (*env)->GetStringUTFChars(env, comment, NULL);
	const char *in_outfile_path = (*env)->GetStringUTFChars(env, outfilepath, NULL);
	
	int comment_len = strlen(in_comment);
	
	//
	int ret = put_user_comment(in_infile_path, in_comment, comment_len, in_outfile_path);
	return (ret == 0);
}
 */
 
JNIEXPORT jint JNICALL Java_net_alaif_android_exif_UserComment_putExifUserComment(JNIEnv *env, jobject obj, jbyteArray srcpath, jbyteArray comment, jbyteArray dstpath)
{
	jbyte *src = (*env)->GetByteArrayElements(env, srcpath, NULL);
	jbyte *dst = (*env)->GetByteArrayElements(env, dstpath, NULL);
	jbyte *com = (*env)->GetByteArrayElements(env, comment, NULL);
	
	int comment_len = strlen((char*)com);
	
	int ret = put_user_comment((char*)src, (char*)com, comment_len, (char*)dst);
	if (ret == 0) {
		//FileCopy
		if (rename(dst, src) == -1) {
			ret = -1;	//コピー失敗
		}
	}
	
	(*env)->ReleaseByteArrayElements(env, srcpath, src, 0);
	(*env)->ReleaseByteArrayElements(env, dstpath, dst, 0);
	(*env)->ReleaseByteArrayElements(env, comment, com, 0);

	return (ret == 0);
}
