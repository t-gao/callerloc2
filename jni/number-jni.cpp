
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "NumberInfo.h"


extern "C" {
   JNIEXPORT jbyteArray JNICALL Java_com_tony_callerloc2_retrievers_NativeRetriever_getLocationFromJni(JNIEnv* env,jclass tis,jstring filename,jstring number);
};



JNIEXPORT jbyteArray JNICALL Java_com_tony_callerloc2_retrievers_NativeRetriever_getLocationFromJni(JNIEnv* env,jclass tis,jstring filename,jstring number)
{

    jboolean iscopy;
	  const char* fnData=(env)->GetStringUTFChars( filename, &iscopy);//get binary file name
	  const char* num = (env)->GetStringUTFChars(number, &iscopy); //get number
	  int phoneNum = 0;	
	  sscanf(num,"%7d",&phoneNum);
	NumberInfoAction action;
    char* location = action.GetCityNameByNumber(fnData,phoneNum);// get result
    jbyte *by = (jbyte*)location;
    jbyteArray jarray = env->NewByteArray(strlen(location));// change to jbytearray
    env->SetByteArrayRegion(jarray,0,strlen(location),by);
    return jarray;
}
