#include <jni.h>
#include <stdio.h>
#include <string.h>
//#include "TestJNIPrimitive.h"


static jboolean CelsiusOrFahrenheit=true;



JNIEXPORT void JNICALL
Java_com_example_huajunzhang_myapp_MainActivity_switchForm(JNIEnv *env, jclass type, jboolean CorF) {
    CelsiusOrFahrenheit=CorF;
}


JNIEXPORT jdouble JNICALL
Java_com_example_huajunzhang_myapp_Function_toFahrenheit(JNIEnv *env, jclass type, jdouble n1) {
    jdouble result;
    result = ((jdouble)n1) * 9/5 + 32;
    return result;
}

JNIEXPORT jboolean JNICALL
Java_com_example_huajunzhang_myapp_Function_tempForm(JNIEnv *env, jclass type) {
    return CelsiusOrFahrenheit;
}