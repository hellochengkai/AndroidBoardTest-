/*
 * adaptor_define.h
 *
 *   Created on: 2017年9月13日
 *       Author: qian
 *  Description: 此文件中只添加底层定义的结构体和枚举、宏定义、常量，不在此文件中声明函数
 */

#ifndef INCLUDE_ADAPTOR_DEFINE_H_
#define INCLUDE_ADAPTOR_DEFINE_H_

typedef struct sRECT
{
    int x;
    int y;
    unsigned int width;
    unsigned int height;
} sRECT;


typedef struct bsurface_memory
{
	void *buffer; /* pointer to mapped memory. Directly accessible by the application.
						This must be typecasted to the correct pixel format, for example
						unsigned short * for a 16 bit pixel format or unsigned long *
						for a 32 bit pixel format. */
	unsigned int pitch; /* The memory width of the surface in bytes. The visible width
							is <= pitch. Pitch must be used to calculate the next
							line of the surface. See the example in the bsurface_get_memory
							documentation. */
} bsurface_memory;



typedef struct bsurface_create_settings
{
	unsigned int pixel_format;	/* format of the surface. */
	unsigned int width;						/* visible width of the surface. The pitch
											may be wider. */
	unsigned int height;					/* visible height of the surface. */

	unsigned int alignment; /* optional buffer alignment specified
				 * as a power of 2, measured in bytes.
				 * 0 is default alignment (default), 1
				 * is 2-byte aligned, 2 is 4-byte
				 * aligned, etc. */

	unsigned int pitch; /* optional buffer pitch, measured in
			     * bytes. 0 is the default pitch (width *
			     * sizeof(pixel)). */
} bsurface_create_settings;


typedef enum
{
	mirror_vertical = 0,	/*垂直镜像*/
	mirror_horizontal,	/*水平镜像*/
	mirror_vh,	/*垂直水平皆镜像*/
	mirror_none	/*不镜像*/
} mirror_settings;

typedef enum
{
	rotation_0 = 0,	/*顺时针旋转0度*/
	rotation_90,	/*顺时针旋转90度*/
	rotation_180,	/*顺时针旋转180度*/
	rotation_270,	/*顺时针旋转270度*/
} rotation_settings;

typedef enum
{
	zorder_moveup = 0,
	zorder_movedown
}zorder_settings;

typedef struct bdecode_window_settings
{
    sRECT position;      /* Position and size on the display. Use display-relative coordinates. */
    unsigned int visible;                /* Is the window visible? */
    unsigned int zorder;             /* not use now. */
    rotation_settings rotation;
    mirror_settings mirror;
} bdecode_window_settings;

typedef struct bsurface_settings
{
	sRECT position; /* Position and size of the surface.
		position.width and height are readonly */
	unsigned int visible; /* Defaults to false. On magnum platforms, this causes a
		surface to be automatically blitted to the framebuffer when bgraphics_sync
		is called. On legacy platforms, this causes the surface to be blended into
		the display by the compositor. */
	unsigned char alpha; /* 0x00 to 0xFF where 0xFF is opaque and 0x00 is transparent.
		On magnum platforms, this is only used when automatically blitting to
		the framebuffer with bgraphics_sync. On legacy platforms, this controls
		the alpha blending with the compositor. */
} bsurface_settings;

enum
{
	HDMIOUTPUTMODE_480 = 0,
	HDMIOUTPUTMODE_720,
	HDMIOUTPUTMODE_1080,
	HDMIOUTPUTMODE_1080I,
	HDMIOUTPUTMODE_4K,
};
enum
{
	VGA_FIRSTWINDOW_8000 = 0,
	VGA_SECONDWINDOW_8000,
	VGA_ThirdWINDOW_8000,
	TV_FIRSTWINDOW_8000,
	TV_SECONDWINDOW_8000,
	TV_ThirdWINDOW_8000,
};
enum
{
	VGA_FIRSTWINDOW_7000 = 0,
	VGA_SECONDWINDOW_7000,
	TV_FIRSTWINDOW_7000,
	TV_SECONDWINDOW_7000,
};

/**define display margin stucture*/
/**CNcomment:定义显示空白区域结构体 */
typedef struct hiUNF_DISP_OFFSET_S
{
    unsigned int u32Left;    /**<left offset *//**<CNcomment:左侧偏移*/
    unsigned int u32Top;     /**<top offset *//**<CNcomment:上方偏移*/
    unsigned int u32Right;   /**<right offset *//**<CNcomment:右侧偏移*/
    unsigned int u32Bottom;  /**<bottom offset *//**<CNcomment:下方偏移*/
}HI_UNF_DISP_OFFSET_S;

enum
{
	DISPLAY_VGA = 0,
	DISPLAY_TV
};


/**
BSURFACE_BLEND_XXXX are values to be used in the bsurface_blit operation
parameter. You should chose one value.

Alpha values are always relative to src1 and should be between 0x00 and 0xFF.
Therefore alpha of 0xFF means take all of src1, none of src2.
Likewise alpha of 0x00 means take none of src1, all of src2.

There are some special cases which are enforced and optimized in the implementation.
For instance, if you don't want to blend colors, you can set src2 to NULL,
use BSURFACE_BLEND_WITH_SRC1_ALPHA, and set pixel1 == 0xFF, and the color blend
will be bypassed. You can still use the BSURFACE_SET_DEST_ALPHA to manipulate
the alpha-per-pixel.
**/

/* Use the pixel1 parameter to blend the sources. */
#define BSURFACE_BLEND_WITH_PIXEL1			0x0000
/* Copy src1's alpha-per-pixel to blend the sources */
#define BSURFACE_BLEND_WITH_SRC1_ALPHA		0x0001
/* Copy src2's alpha-per-pixel to blend the sources */
#define BSURFACE_BLEND_WITH_SRC2_ALPHA		0x0002
/* Blend two palettized surfaces using colorkey to select per pixel */
#define BSURFACE_SELECT_PALETTE_WITH_COLORKEY 0x0003
/* Copy source (blend with one) */
#define BSURFACE_BLEND_WITH_ONE		        0x0004

/**
BSURFACE_BLIT_DEST_ALPHA_XXX specifies how the alpha-per-pixel values of the dest
surface should be filled. If the dest has no alpha-per-pixel, any setting is ignored.
**/

/* Copy the pixel2 parameter into the dest's alpha-per-pixel. */
#define BSURFACE_SET_DEST_ALPHA_WITH_PIXEL2			0x0000
/* Copy src1's alpha channel into dest's alpha-per-pixel. */
#define BSURFACE_SET_DEST_ALPHA_WITH_SRC1_ALPHA		0x0010
/* Copy src2's alpha channel into dest's alpha-per-pixel. */
#define BSURFACE_SET_DEST_ALPHA_WITH_SRC2_ALPHA		0x0020
/* Blend src1's and src2's alpha-per-pixel into dest's alpha-per-pixel.
The formula is dest = src1 + src2 * (1-src1). */
#define BSURFACE_SET_DEST_ALPHA_WITH_AVG_ALPHA		0x0030
/* Blend pixel2 parameter and src2's alpha-per-pixel into dest's alpha-per-pixel.
The formula is dest = pixel2 + src2 * (1-pixel2). */
#define BSURFACE_SET_DEST_ALPHA_WITH_AVG_ALPHA_PIXEL2   0x0040
 /* Multiply src1's alpha-per-pixel with pixel2 parameter.
The formula is dest = src1 * pixel2. */
#define BSURFACE_SET_DEST_ALPHA_WITH_SRC1_TIMES_PIXEL2  0x0050

/**
Summary:
Blit two surfaces together into a destination surface.

Desription:
Blit is a superset of copy. It gives you more control over how two surfaces
will be blended together and how the destination surface's alpha-per-pixel (or
alpha channel) will be populated if present.

src1 or src2 can be the same as dest.

If src2 is NULL, then you must use BSURFACE_BLEND_WITH_SRC1_ALPHA and
have pixel1 of 0xFF. Anything else implies that you are blending between src1
and src2, which cannot be done. Likewise for src1.

If both src1 and src2 are null, then operation is ignored and pixel1 is regarded
as a fill color.

There are some operations that will internally result as a two-pass operation.
This is hardware specific. In general, the Settop API will fulfill your request
or return an error code if it cannot. Please see the implementation for more
details.

Scaling and clipping are specified by the proportion of the src1, src2 and
dest rectangles. The entire src1_rect is combined with the entire src2_rect and
the result is copied into the entire dest_rect. Some platforms have
scaling limitations. For instance, on 7038 src2 cannot scale, therefore its
width and height must match dest's.

Example:
	// bsurface_copy is equivalent to:
	bsurface_blit(destsurface, destrect,
		BSURFACE_BLEND_WITH_PIXEL1|BSURFACE_SET_DEST_ALPHA_WITH_SRC1_ALPHA,
		src1, src1rect, NULL, NULL, 0xFF, 0xFF);

	// blend two surfaces together using a constant alpha, and set
	// the dest's alpha channel with a constant alpha
	bsurface_blit(destsurface, destrect,
		BSURFACE_BLEND_WITH_PIXEL1|BSURFACE_SET_DEST_ALPHA_WITH_PIXEL2,
		src1, src1rect, src2, src2rect, 0xA0, 0xFF);

	// blend two surfaces together using src1's alpha channel, and set
	// the dest's alpha channel to be src1's alpha. The pixel1 and pixel2
	// values are ignored.
	bsurface_blit(destsurface, destrect,
		BSURFACE_BLEND_WITH_SRC1_ALPHA|BSURFACE_SET_DEST_ALPHA_WITH_SRC1_ALPHA,
		src1, src1rect, src2, src2rect, 0, 0);
**/


enum
{
	AUDIOTYPE_WAV = 0,
	AUDIOTYPE_MP3
};
//回放 播放混音要支持 wav 和 MP3 两种格式


typedef enum {
        SP_PIC_TYPE_JPEG = 0,
        SP_PIC_TYPE_PNG,
		SP_PIC_TYPE_BMP
} sp_pic_type;

//中断刷新
enum
{
	WAITVBLANK_TV = 0,
	WAITVBLANK_VGA
};

typedef enum {
        STATUS_STOP = 0,
        STATUS_RUN,
        STATUS_BUTT
} TS_HDMIRX_STATUS_E;
extern TS_HDMIRX_STATUS_E TS_HDMI_Status;


typedef  enum {
        MID_STOP_MODE_STILL = 0,
        MID_STOP_MODE_BLACK
} mid_stop_mode;


#define OPENGL_VGA 0
#define OPENGL_OSD 1


enum
{
	DECODER_0 = 0,
	DECODER_1
};

#endif /* INCLUDE_ADAPTOR_DEFINE_H_ */
