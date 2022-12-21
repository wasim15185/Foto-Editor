//#include "header.rsh"

#pragma version(1)
#pragma rs java_package_name(com.android.demoeditor)
  #pragma rs_fp_full

//  #pragma rs_fp_relaxed



static float bright = 0.f;

void setBright(float v) {
    bright = 255.f / (255.f - v);
}


 //uchar4 __attribute__((kernel)) root(uchar4 in ){

//Convert input uchar4 to float4
   //  float4 f4 = rsUnpackColor8888(in);

  //  float red = bright * f4.r ;
  //  float green = bright * f4.g ;
  //  float blue= bright * f4.b ;

  //  return rsPackColorTo8888(red,green,blue ,f4.a) ;
// }


// uchar4 RS_KERNEL brightness(const uchar4 in) {
// uchar4 out = in;

 // out.r = clamp((int)(bright * in.r), 0, 255);
  //  out.g = clamp((int)(bright * in.g), 0, 255);
   //  out.b = clamp((int)(bright * in.b), 0, 255);

   // return out;
 //}




//Function to compute brightness value
  void brightness(const uchar4 *in, uchar4 *out){
     out->r = clamp((int)(bright * in->r), 0, 255);
     out->g = clamp((int)(bright * in->g), 0, 255);
     out->b = clamp((int)(bright * in->b), 0, 255);
  }