package com.aispeech;

public class AIAudioRecord {
    static {
        System.loadLibrary("aispeechaudio");
    }

    public native final int _native_setup(int audioSource, int sampleRate, int channelNum);

    public native final int _native_start();

    public native final int _native_stop();

    public native final int _native_read_in_byte_array(byte[] audioData, int offsetInBytes, int sizeInBytes);


}
