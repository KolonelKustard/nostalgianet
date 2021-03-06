/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class craigl_winplayer_WinPlayer */

#ifndef _Included_craigl_winplayer_WinPlayer
#define _Included_craigl_winplayer_WinPlayer
#ifdef __cplusplus
extern "C" {
#endif
#undef craigl_winplayer_WinPlayer_ALLTYPES
#define craigl_winplayer_WinPlayer_ALLTYPES -1L
#undef craigl_winplayer_WinPlayer_NOTYPE
#define craigl_winplayer_WinPlayer_NOTYPE 0L
#undef craigl_winplayer_WinPlayer_SOURCE
#define craigl_winplayer_WinPlayer_SOURCE 1L
#undef craigl_winplayer_WinPlayer_PROCESSOR
#define craigl_winplayer_WinPlayer_PROCESSOR 2L
#undef craigl_winplayer_WinPlayer_MONITOR
#define craigl_winplayer_WinPlayer_MONITOR 3L
#undef craigl_winplayer_WinPlayer_SINK
#define craigl_winplayer_WinPlayer_SINK 4L
#undef craigl_winplayer_WinPlayer_MINSAMPLERATE
#define craigl_winplayer_WinPlayer_MINSAMPLERATE 8000L
#undef craigl_winplayer_WinPlayer_MAXSAMPLERATE
#define craigl_winplayer_WinPlayer_MAXSAMPLERATE 44100L
#undef craigl_winplayer_WinPlayer_DEFAULTSAMPLERATE
#define craigl_winplayer_WinPlayer_DEFAULTSAMPLERATE 11025L
/* Inaccessible static: buffer */
/* Inaccessible static: initComplete */
/* Inaccessible static: resetMode */
/*
 * Class:     craigl_winplayer_WinPlayer
 * Method:    nativeSelectDevice
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_craigl_winplayer_WinPlayer_nativeSelectDevice
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     craigl_winplayer_WinPlayer
 * Method:    nativePlay
 * Signature: (Lcraigl/winplayer/WinPlayer;)V
 */
JNIEXPORT void JNICALL Java_craigl_winplayer_WinPlayer_nativePlay
  (JNIEnv *, jclass, jobject);

/*
 * Class:     craigl_winplayer_WinPlayer
 * Method:    nativeReset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_craigl_winplayer_WinPlayer_nativeReset
  (JNIEnv *, jclass);

/*
 * Class:     craigl_winplayer_WinPlayer
 * Method:    nativeClose
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_craigl_winplayer_WinPlayer_nativeClose
  (JNIEnv *, jclass);

/*
 * Class:     craigl_winplayer_WinPlayer
 * Method:    nativeStoreSamples
 * Signature: ([SI)V
 */
JNIEXPORT void JNICALL Java_craigl_winplayer_WinPlayer_nativeStoreSamples
  (JNIEnv *, jclass, jshortArray, jint);

#ifdef __cplusplus
}
#endif
#endif
