
#pragma version(1)
#pragma rs java_package_name(com.android.demoeditor)
 #pragma rs_fp_full

float contrast = 0.f;

/*
 * RenderScript kernel that performs contrast manipulation using Escher's approach.
 */
uchar4 __attribute__((kernel)) contrastness(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float4 t = (contrast * f4) + 0.5 * (1 - contrast);
    return rsPackColorTo8888(t);
}