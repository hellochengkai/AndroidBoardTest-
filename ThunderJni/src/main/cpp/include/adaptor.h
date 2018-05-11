

#ifndef __ADAPTOR_H__
#define __ADAPTOR_H__

#include "adaptor_define.h"
#include <android/native_window.h>

typedef void (*Mid_DEBUG_CALLBACK)(char* filename, char *funcname, int line, char* info);

int Mid_RegsiterDEBUGCallbackFunction(Mid_DEBUG_CALLBACK callback_function);

//功能说明: 所有初始化工作都集成到这个接口里
//参数说明:
//返回值: 无
void Mid_KtvSysInit();
//功能说明: 设置视频输出制式
//参数说明:
//	nMode: 0:N制 1:N/P 自动切换
//返回值: 无
void Mid_MpegDecoderNtscPalSwitch(int nMode);
//功能说明: 设置HDMI输出分辨率
//参数说明:
//	nOutPutMode: 0:480 1:720 2:1080
//返回值: 无
void Mid_SetHDMIOutPutMode(int nOutPutMode);

//功能说明: 获得视频的窗口信息
//参数说明:
//	nWindowIndex: 代表是哪一个视频窗口，
//返回值: 无
void Mid_GetDecodeWindow(int nWindowIndex, bdecode_window_settings *settings);

//功能说明: 设置视频的窗口信息
//参数说明:
//	nWindowIndex: 代表是哪一个视频窗口，
//	settings: 视频窗口信息结构
//返回值: 无

void Mid_SetDecodeWindow(int nWindowIndex, bdecode_window_settings *settings);
//功能说明: 设置视频的窗口ZORDER
//参数说明:
//	nWindowIndex: 代表是哪一个视频窗口，
//	zorder: 视频窗口的z序
//返回值: 无
void Mid_SetDecoderWindowZoder(int nWindowIndex, zorder_settings zorder);


int Mid_TS_VoutSendFrameToNavieWindow(int stream_id, ANativeWindow *native_window);

int Mid_Get_VGA_FIRSTWINDOW_Index();
int Mid_Get_TV_FIRSTWINDOW_Index();

//功能说明: 设置图形层的信息
//参数说明:
//	nDisplay: 代表是哪一个DISPLAY, 0:VGA  1:TV
//     nFBIndex: 代表哪一层FB
//	nSurface: Surface
//返回值: 无
void Mid_SetGraphicsFramebuffer(int nDisplayIndex, int nFBIndex, int nSurface);

//功能说明: 获取Grahpics Surface
//参数说明:
//	nDisplay: 代表是哪一个DISPLAY, 0:VGA  1:TV
//     nFBIndex: 代表哪一层FB
//返回值: Surface
int Mid_GetGraphicsFramebufferSurface(int nDisplayIndex, int nFBIndex);

//设置TV端显示的图层
int Mid_Graphics_SetFrameBuffer(int surface);

//获取TV端显示的图层 fb_id: 0 , 1
int Mid_Surface_GetFromFB(int fb_id);

//功能说明: 设置图形层的Z序
//参数说明:
//	nDisplay: 代表是哪一个DISPLAY, 0:VGA  1:TV
//     nFBIndex: 代表哪一层FB
//	zorder: z序
//返回值: 无
void Mid_SetGraphicsZorder(int nDisplayIndex, int nFBIndex, zorder_settings zorder);

//功能说明: 设置图形层的alpha
//参数说明:
//	nDisplay: 代表是哪一个DISPLAY, 0:VGA  1:TV
//     nFBIndex: 代表哪一层FB
//	alpha:透明度
//返回值: 无
void Mid_SetGraphicsAlpha(int nDisplayIndex, int nFBIndex, int alpha);


//功能说明: 申请Surface
//参数说明:
//	nWidth: Surface的宽度
//	nHight: Surface的高度
//返回值: Surface的句柄, 大于0: 申请成功   等于0: 申请失败
int Mid_CreateSurface(int nWidth, int nHight);

//功能说明: 释放Surface
//参数说明:
//	nSurface: Surface句柄
//返回值: 无
void Mid_DestorySurface(int nSurface);

//功能说明: 获得Surface的信息
//参数说明:
//	nSurface: Surface句柄
//	settings: 存放Surface信息的结构指针
//返回值: 无
void Mid_GetSurfaceInfo(int nSurface, bsurface_settings *settings);

//功能说明: 获得Surface的bsurface_memory结构
//参数说明:
//	nSurface: Surface句柄
//	memory:	存放Surface的bsurface_memory结构指针
//返回值: 无
void Mid_GetSurfaceMemory(int nSurface, bsurface_memory *memory);

//功能说明: surface按区域填充固定颜色值
//参数说明:
//	nSurface: Surface句柄
//	rect: 填充区域
//返回值: 0：失败  1：成功
int Mid_SurfaceFill(int nSurface,  const sRECT *rect, unsigned int pixel);

//功能说明: surface按区域拷贝
//参数说明:
//	destsurface: 目标Surface
//	destrect: 目标区域
//	srcsurface: 源Surface
//	destrect: 源区域
//返回值: 0：失败  1：成功
int Mid_SurfaceCopy(int destsurface, const sRECT *destrect, int srcsurface, const sRECT *srcrect);


//功能说明: surface按区域镜像
//参数说明:
//	destsurface: 目标Surface
//	destrect: 目标区域
//	srcsurface: 源Surface
//	destrect: 源区域
//	mirror: 0:垂直镜像 1:水平镜像 2:垂直水平镜像
//返回值: 0：失败  1：成功
int Mid_SurfaceMirror(int destsurface, const sRECT *destrect, int srcsurface, const sRECT *srcrect, mirror_settings mirror);
//功能说明: surface混合(用法请看接口下面的注释)
//参数说明:
//	destsurface: 目标Surface
//	destrect: 目标区域
//  operation: 混合参数
//	src1: 源Surface1
//	src1_rect: 源区域1
//	src2: 源Surface2
//	src2_rect: 源区域2
//	pixel1: 源Surface2
//	pixel2: 源区域2
//返回值: 0：失败  1：成功
int Mid_SurfaceBlit(int destsurface, const sRECT *destrect, unsigned int operation, int src1, const sRECT *src1_rect, int src2, const sRECT *src2_rect, unsigned int pixel1, unsigned int pixel2);


//******************视频播放器相关******************
//功能说明: 设置音频输出为哪一路解码的声音
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
void Mid_MpegDecoderSetAudioOutput(int nDecodeID);

//功能说明: 开始播放视频
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nMode:  播放模式 1：简单模式播放  0：正常模式播放
//返回值: 0：失败  1：成功
int Mid_MpegDecoderPlay(int nDecodeID, int nMode);

//功能说明: 结束播放视频
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderStop(int nDecodeID);
//功能说明: 获得解码缓冲区
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  p:  缓冲区指针
//  dwBufLen: 获得的缓冲区的大小(如果暂停了的话，获得缓冲区的大小应该为0)
//返回值: -1:失败  0：成功  1：wait for buffer
int Mid_MpegDecoderGetBuffer(int nDecodeID, unsigned char** p, int* dwBufLen);

//功能说明: 播放解码缓冲区中的数据
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  p:  缓冲区指针
//  dwBufLen: 缓冲区中数据的大小
//返回值: 0：失败  1：成功
int Mid_MpegDecoderPushBuffer(int nDecodeID, unsigned char* p, int dwBufLen);


//功能说明: 清除播放缓冲区的数据
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderFlushBuffer(int nDecodeID);

//功能说明: 判断缓冲区的数据是否播完
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：没有播放完毕  1：播放完毕
//判断缓冲区的数据是否播完 0:没有播完  1:播完了
int Mid_MpegDecoderWaitingForPlayEnd(int nDecodeID);
//功能说明: 获得视频的播放时间
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderGetVideoPts(int nDecodeID,unsigned long *pPTS);
//功能说明: 获得音频的播放时间
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderGetAudioPts(int nDecodeID,unsigned long *pPTS);

//功能说明: 暂停
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderPause(int nDecodeID);

//功能说明: 播放
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 0：失败  1：成功
int Mid_MpegDecoderResume(int nDecodeID);

//功能说明: 获得当前播放视频的音轨数
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//返回值: 音轨数
int Mid_MpegDecoderGetAudioStreamNumber(int nDecodeID);
//功能说明: 单音轨歌曲设置声道
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  dwMode:  0:左声道 1:右声道  2:立体声
//返回值: 0：失败  1：成功
int Mid_MpegDecoderSetAudioMode(int nDecodeID,int dwMode);

//功能说明: 多音轨歌曲设置音轨从0开始
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nStreamIndex:  音轨索引
//返回值: 0：失败  1：成功
int Mid_MpegDecoderSelectAudioStream(int nDecodeID,int nStreamIndex);
//功能说明: 设置输出音量（0-100）
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nVol:  音量值（0-100）
//返回值: 0：失败  1：成功
int Mid_MpegDecoderSetVolume(int nDecodeID,int nVol);
//功能说明: 设置静音、放音
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nMute:  1:静音 0:放音
//返回值: 0：失败  1：成功
int Mid_MpegDecoderSetMute(int nDecodeID,int nMute);
//功能说明: 设置音调
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nKey:  范围为-12 - 12
//返回值: 0：失败  1：成功
int Mid_MpegDecoderSetAuidoKey(int nDecodeID,int nKey);
//功能说明: 获得播放视频的图像
//参数说明:
//	nDecodeID: 第几路视频0:第一路视频 1:第二路视频
//  nWidth:  Surface宽度
//  nHight:  Surface高度
//返回值: Surface
int Mid_GetSurfaceFromDecode(int nDecodeID, int* nWidth, int* nHight);
//功能说明: 硬解JPEG和PNG为Surface
//参数说明:
//	pic_type: 0:jpeg 1:png
//  buffer:  图片的buffer
//  size:  图片的size
//返回值: Surface
int Mid_GetSurfaceFromPicture(int pic_type, unsigned char * buffer, int size);

//播放网络视频
int Mid_FileStreamPlay(int stream_id, unsigned char *file_path, int startAuidoIndex);
//获取视频总时间
int Mid_FileStreamGetTotalTime(int stream_id, unsigned long *time_ms);
//停止视频播放
int Mid_FileStreamStop(int stream_id);
//清空缓存区
int Mid_FileStreamFlushBuffer(int stream_id);
//检测是否播放结束
int Mid_FileStreamCheckPlayEnd(int stream_id);
//获取当前播放时间
int Mid_FileStreamGetPlayTime(int stream_id, unsigned long *time_ms);

//暂停播放
int Mid_FileStreamPause(int stream_id);
//恢复播放
int Mid_FileStreamResume(int stream_id);
//获取当前播放时间戳
int Mid_FileStreamGetVideoFirstPTS(int stream_id, unsigned long *time_ms);
int Mid_FileStreamGetAudioFirstPTS(int stream_id, unsigned long *time_ms);
int Mid_FileStreamGetVideoPTS(int stream_id,unsigned long *time_ms);
int Mid_FileStreamGetAudioPTS(int stream_id,unsigned long *time_ms);
//******************视频播放器相关end******************

//******************录音相关******************
//功能说明: 设置录音音量
//参数说明:
//	nVolume: 录音音量
//返回值: 0：失败  1：成功
int Mid_SetRecordVolume(int nVolume);
//功能说明: 开始录音
//参数说明:
//返回值: 0：失败  1：成功
int Mid_DecoderAnalogAudioStart();
//功能说明: 获取采样率
//参数说明:
//返回值: 采样率大小
int Mid_Get_Audio_Sample_Rate();

//功能说明: 获得录音数据缓冲区
//参数说明:
//  pBuffer: 录音数据缓冲区指针
//  p:  录音数据大小
//返回值: 0：失败  1：成功
int Mid_GetRecordBuf(char ** pBuffer, int* p);

//功能说明: 读取录音数据到缓冲区中
//参数说明:
//  nSize: 录音数据大小
//返回值: 0：失败  1：成功
int Mid_PCMRecordReadComplete(int nSize);
//功能说明: 录音完毕
//参数说明:
//返回值: 0：失败  1：成功
int Mid_DecoderAnalogAudioStop();
//******************录音相关 end******************

//******************混音相关******************
//废弃
//******************混音相关 end******************

unsigned int  Mid_MpegDecoderGetFirstVideoPts(int decode_id, int passed);

/*
	设置机顶盒音调，数值为-12~12
*/
int Mid_StreamSetAudioKey(int stream_id, int key);

/*
	获取机顶盒音调
*/
int Mid_StreamGetAudioKey(int stream_id, int *key);

void Mid_WhichAudioTrackYouWantToStart(int channel);



//**********************************
int Mid_Encoder_Open(int target_width, int target_height, int target_bitrate, char *output_path1, char *output_path2);
int Mid_Encoder_Start();
int Mid_Encoder_Stop();
int Mid_Encoder_Close();
//******************end******************


//********************************
int Mid_SetOutputEnable(int nDecodeID,int enable);
int Mid_VideoWindow_Drop(int enable);
//****************end*************

/*
 * 开启将VGA换影到TV窗口
 */
int Mid_ScreenCast_Start();

/*
 * 停止将VGA换影到TV窗口
 */
int Mid_ScreenCast_Stop();
/**
 * 释放播放器申请的内存
 */
int Mid_Output_Deinit();
int Mid_SYS_Deinit();
int Mid_SetGPIO(int gpio,int value);
int Mid_InitI2C1();
int Mid_I2CWrite(char u8DevAddress, int u32RegAddr,
                        int u32RegAddrCount, char * pu8Buf, int u32Length );

int Mid_I2CRead(char u8DevAddress, int u32RegAddr,
                       int u32RegAddrCount, char *pu8Buf, int u32Length);
int Mid_DeInitI2C1();
#endif //__ADAPTOR_H__


