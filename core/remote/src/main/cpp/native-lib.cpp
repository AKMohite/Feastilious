#include <jni.h>
#include <vector>
#include <string>
#include <algorithm>
#include "aes.h"

#define ROTATE_BY 5

// Undo rotation used in generator
static void undoRotation(std::vector<uint8_t>& data) {
    std::rotate(data.rbegin(), data.rbegin() + ROTATE_BY, data.rend());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mak_feastit_remote_di_CapillaryModule_getApiKey(
        JNIEnv *env,
        jobject /* this */) {

    // Encrypted parts from generator
    const uint8_t part1[] = { 0x36, 0x94, 0x24, 0x4e, 0x05, 0xfa, 0x29, 0x82 };
    const uint8_t part2[] = { 0xd7, 0xf6, 0xf7, 0xfd, 0xf3, 0x0d, 0x79, 0x26 };
    const uint8_t part3[] = { 0xb5, 0xc3, 0xf3, 0xdc, 0x11, 0x53, 0xdd, 0x63 };
    const uint8_t part4[] = { 0xf2, 0x66, 0xc0, 0x8c, 0xa0, 0x09, 0xa5, 0x53 };

    std::vector<uint8_t> buffer;
    buffer.insert(buffer.end(), part1, part1 + sizeof(part1));
    buffer.insert(buffer.end(), part2, part2 + sizeof(part2));
    buffer.insert(buffer.end(), part3, part3 + sizeof(part3));
    buffer.insert(buffer.end(), part4, part4 + sizeof(part4));

    // Undo rotation
    undoRotation(buffer);

    // AES key & IV
    uint8_t key[16] = { 0x10,0x23,0x44,0x55,0x66,0x77,0x88,0x99,
                        0xAA,0xBB,0xCC,0xDD,0xEE,0xFF,0x11,0x22 };
    uint8_t iv[16] = { 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
                       0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x10 };

    // AES-CBC decrypt
    AES_ctx ctx;
    AES_init_ctx_iv(&ctx, key, iv);
    AES_CBC_decrypt_buffer(&ctx, buffer.data(), buffer.size());

    // Remove PKCS#7 padding
    if (!buffer.empty()) {
        uint8_t pad = buffer.back();
        if (pad > 0 && pad <= 16 && pad <= buffer.size()) {
            buffer.resize(buffer.size() - pad);
        }
    }

    // Convert to std::string (ASCII)
    std::string apiKey(buffer.begin(), buffer.end());

    // Return safely
    return env->NewStringUTF(apiKey.c_str());
}
