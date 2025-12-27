#include <jni.h>
#include <vector>
#include <string>
#include <algorithm>
#include <sys/ptrace.h>
#include "aes.h"

#define ROTATE_BY 5

// Optional: detect debugger
bool isDebuggerAttached() {
    return ptrace(PTRACE_TRACEME, 0, nullptr, 0) == -1;
}

// Correct rotation to undo generator's rotation
void undoGeneratorRotation(std::vector<uint8_t>& data) {
    // Generator rotates using rbegin(), this is its exact inverse
    std::rotate(data.rbegin(), data.rbegin() + ROTATE_BY, data.rend());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mak_feastit_remote_di_CapillaryModule_getApiKey(JNIEnv *env, jobject) {

    // Uncomment for release builds
//    if (isDebuggerAttached()) {
//        return env->NewStringUTF("");
//    }

    // 🔐 Paste generated encrypted parts here
    std::vector<uint8_t> part1 = { 0x36, 0x94, 0x24, 0x4e, 0x05, 0xfa, 0x29, 0x82 };
    std::vector<uint8_t> part2 = { 0xd7, 0xf6, 0xf7, 0xfd, 0xf3, 0x0d, 0x79, 0x26 };
    std::vector<uint8_t> part3 = { 0xb5, 0xc3, 0xf3, 0xdc, 0x11, 0x53, 0xdd, 0x63 };
    std::vector<uint8_t> part4 = { 0xf2, 0x66, 0xc0, 0x8c, 0xa0, 0x09, 0xa5, 0x53 };

    // Combine all parts
    std::vector<uint8_t> encrypted;
    encrypted.insert(encrypted.end(), part1.begin(), part1.end());
    encrypted.insert(encrypted.end(), part2.begin(), part2.end());
    encrypted.insert(encrypted.end(), part3.begin(), part3.end());
    encrypted.insert(encrypted.end(), part4.begin(), part4.end());

    // 🔄 Undo rotation
    undoGeneratorRotation(encrypted);

    // AES key & IV (must match generator)
    uint8_t key[16] = {
            0x10,0x23,0x44,0x55,
            0x66,0x77,0x88,0x99,
            0xAA,0xBB,0xCC,0xDD,
            0xEE,0xFF,0x11,0x22
    };

    uint8_t iv[16] = {
            0x01,0x02,0x03,0x04,
            0x05,0x06,0x07,0x08,
            0x09,0x0A,0x0B,0x0C,
            0x0D,0x0E,0x0F,0x10
    };

    // 🔐 Decrypt using TinyAES
    AES_ctx ctx;
    AES_init_ctx_iv(&ctx, key, iv);
    AES_CBC_decrypt_buffer(&ctx, encrypted.data(), encrypted.size());

    // Optional: remove PKCS#7 padding (if generator uses it)
    uint8_t pad = encrypted.back();
    if (pad > 0 && pad <= 16) {
        encrypted.resize(encrypted.size() - pad);
    }

    // Convert to string and return
    std::string apiKey(encrypted.begin(), encrypted.end());
    return env->NewStringUTF(apiKey.c_str());
}
