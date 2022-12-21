#ifndef __RS_CLAMP_RSH__
#define __RS_CLAMP_RSH__

static inline int FClamp(const int t, const int tLow, const int tHigh) {
    if (t < tHigh) {
        return ((t > tLow) ? t : tLow);
    }
    return tHigh;
}

static inline float FClampFloat(const float t, const float tLow, const float tHigh) {
    if (t < tHigh) {
        return ((t > tLow) ? t : tLow);
    }
    return tHigh;
}

static inline float3 FClampFloat3(const float3 t, const float tLow, const float tHigh) {
    t.r = FClampFloat(t.r, tLow, tHigh);
    t.g = FClampFloat(t.g, tLow, tHigh);
    t.b = FClampFloat(t.b, tLow, tHigh);
    return t;
}

static inline float FClamp01Float(const float d) {
    return FClampFloat(d, 0.0f, 1.0f);
}

static inline float3 FClamp01Float3(const float3 t) {
    t.r = FClampFloat(t.r, 0.0f, 1.0f);
    t.g = FClampFloat(t.g, 0.0f, 1.0f);
    t.b = FClampFloat(t.b, 0.0f, 1.0f);
    return t;
}

#endif //#ifndef __RS_CLAMP_RSH__