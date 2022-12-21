#pragma version(1)
#pragma rs java_package_name(com.android.demoeditor)
#pragma rs_fp_relaxed


/*
*Do not change Any thing
*/
const static float3 gMonoMult = {0.299f, 0.587f, 0.114f};

float saturationValue = 0.f;

/*
 * RenderScript kernel that performs saturation manipulation.
 */
uchar4 __attribute__((kernel)) saturation(uchar4 in)
{
    float4 f4 = rsUnpackColor8888(in);
    float3 result = dot(f4.rgb, gMonoMult);
    result = mix(result, f4.rgb, saturationValue);

    return rsPackColorTo8888(result);
}