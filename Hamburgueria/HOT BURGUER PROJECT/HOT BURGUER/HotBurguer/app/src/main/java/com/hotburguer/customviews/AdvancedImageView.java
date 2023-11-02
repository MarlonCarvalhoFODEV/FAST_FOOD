package com.hotburguer.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;


import com.hotburguer.R;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AdvancedImageView extends AppCompatImageView {

    private Paint paintSquare;
    private Paint paintArc;
    private Rect rect;

    private Paint mBorderPaint;
    private Paint mBitmapPaint;

    private Bitmap bitmap, overlayBitmap, bitmapBlurred, bitmapMask, bitmapResult, otherBitmap;


    private int mBorderWidth, DEFAULT_BORDER_WIDTH, mBorderColor, mBorderRadius;
    private int gaussianBlur, gaussianBlurDefined, overlapColor, bitmapMaskID, idReference;
    private float arcHeight;

    private static final int SHAPE_CIRCLE = 0;
    private static final int SHAPE_ROUND = 1;
    private static final int SHAPE_DIAGONAL = 2;
    private static final int SHAPE_STAR = 3;
    private static final int SHAPE_HEART = 4;
    private static final int SHAPE_ARC_TOP = 5;
    private static final int SHAPE_WAVE_TOP = 6;


    private boolean mHasBorder;
    private int typeShape, elevation;

    public AdvancedImageView(Context context) {
        super(context);
        init(null);
    }

    public AdvancedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AdvancedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attr) {

        TypedArray ta = getContext().obtainStyledAttributes(attr, R.styleable.AdvancedImageView);

        //BitmapDrawable bitmapDrawable1 = (BitmapDrawable) getDrawable();
        //bitmap = bitmapDrawable1.getBitmap();

        //idSrc = ta.getResourceId(R.styleable.AdvancedImageView_android_src, -1);
        idReference = ta.getResourceId(R.styleable.AdvancedImageView_overlaySrc, -1);
        if (idReference > -1) {
            try {
                Drawable drawable = ta.getDrawable(R.styleable.AdvancedImageView_overlaySrc);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                overlayBitmap = bitmapDrawable.getBitmap();
            } catch (ClassCastException e) {
                overlayBitmap = getBitmapFromVectorDrawable(getContext(), idReference);
            }
        }

        arcHeight = ta.getDimensionPixelSize(R.styleable.AdvancedImageView_shape_arc_height, 20);
        elevation = ta.getDimensionPixelSize(R.styleable.AdvancedImageView_elevation, 0);
        typeShape = ta.getInt(R.styleable.AdvancedImageView_type_shape, -1);

        gaussianBlur = (int) ta.getFloat(R.styleable.AdvancedImageView_blur_radius, 0);

        rect = new Rect();

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSquare.setColor(Color.parseColor("#65FFFFFF"));

        paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc.setColor(Color.WHITE);

        ta.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();

        if (typeShape != -1 && drawable != null) {
            try {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } catch (ClassCastException e) {
                //bitmap = getBitmapFromVectorDrawable(getContext(), idSrc);
            }
            if (bitmap != null && bitmap != otherBitmap) {

                int width = canvas.getWidth();
                int height = canvas.getHeight();

                if (width == 0)
                    width = bitmap.getWidth();
                if (height == 0)
                    height = bitmap.getHeight();

                Log.i("JJDSJJDSJADJSAJ", "onDraw(Canvas canvas)");

                BitmapShader shader = createBitmapShader(bitmap, width, height);
                mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mBitmapPaint.setShader(shader);

                switch (typeShape) {
                    case SHAPE_CIRCLE:
                        setImageBitmap(drawCircle(width, height));
                        break;
                    case SHAPE_DIAGONAL:
                        bitmap = drawDiagonalInverted(width, height);
                        setImageBitmap(bitmap);
                        break;
                    case SHAPE_ROUND:
                        setImageBitmap(drawRound(width, height));
                        break;
                    case SHAPE_STAR:
                        //canvas.drawBitmap(drawStar(canvas, width, height),0,0,null);
                        break;
                    case SHAPE_HEART:
                        drawHeart(canvas, width, height);
                        break;
                    case SHAPE_ARC_TOP:
                        bitmap = drawArcTop(bitmap, width, height);
                        setImageBitmap(bitmap);
                        break;
                    case SHAPE_WAVE_TOP:
                        bitmap = drawWaveTop(bitmap, width, height);
                        setImageBitmap(bitmap);
                        break;
                }

                otherBitmap = bitmap;

                if (overlapColor > 0) {
                    getDrawable().mutate().setColorFilter(getResources().getColor(overlapColor), PorterDuff.Mode.SRC_IN);
                }

                if (gaussianBlur > 0 && otherBitmap != bitmap) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    //otherBitmap = bitmap;

                    gaussianBlurDefined = gaussianBlur;

                    //setImageBitmap(blur(bitmap, gaussianBlur,false));
                } else if (bitmap != otherBitmap && gaussianBlur == 0) {
                    // otherBitmap = bitmap;
                    //setImageBitmap(bitmap);


                }
            }
        }

    }

    public boolean equals(Bitmap bitmap1, Bitmap bitmap2) {
        try {
            ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
            bitmap1.copyPixelsToBuffer(buffer1);

            ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
            bitmap2.copyPixelsToBuffer(buffer2);

            return Arrays.equals(buffer1.array(), buffer2.array());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        return null;
    }

    private BitmapShader createBitmapShader(Bitmap bitmap, int width, int height) {

        if (bitmap != null) {
            return new BitmapShader(Bitmap.createScaledBitmap(bitmap, width, height, false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        return null;
    }

    public void setBlur(int blurRadius) {
        gaussianBlur = blurRadius;
        postInvalidate();
    }

    public synchronized Bitmap blur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    private Bitmap getBitmap(int resource) {
        return BitmapFactory.decodeResource(getResources(), resource);
    }

    private Rect createRect(int left, int top, int right, int bottom) {
        rect.top = top;
        rect.left = left;
        rect.right = right;
        rect.bottom = bottom;
        return rect;
    }

    private Path createArcTop(int width, int height) {

        int newHeight = (int) (height - 50);
        int curve = (int) (arcHeight * 2 + (height - (height - (height - 50))));

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, newHeight);
        path.quadTo(width / 2, curve, width, newHeight);
        path.lineTo(width, 0);
        path.close();

        return path;
    }

    private Path createTopWave(int width, int height) {

        int newHeight = (int) (height - 50);
        int curve = (int) (height * 2 + (height - (height - (height - 50))));

        Path path = new Path();
        Path mpath = new Path();
        mpath.moveTo(0, newHeight);
        mpath.rQuadTo(20, 5, 40, 0);
        mpath.rQuadTo(20, -5, 40, 0);
        path.close();

        return path;
    }

    private synchronized Bitmap drawArcTop(Bitmap bitmap, int width, int height) {
        Path borderPath = createArcTop(width, height);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        Path bitmapPath = createArcTop(bitmapWidth, bitmapHeight);

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        mBorderPaint.setColor(Color.TRANSPARENT);
        mBitmapPaint.setAntiAlias(true);

        Canvas cWriter = new Canvas(output);
        cWriter.drawPath(borderPath, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        mBitmapPaint.setShadowLayer(elevation, 0, 0, Color.DKGRAY);
        cWriter.drawPath(bitmapPath, mBitmapPaint);

        return output;
    }

    private synchronized Bitmap drawWaveTop(Bitmap bitmap, int width, int height) {
        Path borderPath = createTopWave(width, height);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        Path bitmapPath = createTopWave(bitmapWidth, bitmapHeight);

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        mBorderPaint.setColor(Color.TRANSPARENT);
        mBitmapPaint.setAntiAlias(true);

        Canvas cWriter = new Canvas(output);
        cWriter.drawPath(borderPath, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        mBitmapPaint.setShadowLayer(elevation, 0, 0, Color.DKGRAY);
        cWriter.drawPath(bitmapPath, mBitmapPaint);

        return output;
    }

    private Bitmap drawCircle(int width, int height) {
        int borderRadius = Math.min(width, height) / 2;
        int bitmapRadius = borderRadius - mBorderWidth;

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cWriter = new Canvas(output);
        cWriter.drawCircle(borderRadius, borderRadius, borderRadius, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(finalBitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        cWriter.drawCircle(borderRadius, borderRadius, bitmapRadius, mBitmapPaint);

        return output;
    }

    private Bitmap drawRound(int width, int height) {
        RectF borderRect = new RectF();
        borderRect.set(0f, 0f, 50, 50);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        RectF bitmapRect = new RectF();
        bitmapRect.set(mBorderWidth, mBorderWidth, bitmapWidth, bitmapHeight);


        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cWriter = new Canvas(output);
        cWriter.drawRoundRect(borderRect, mBorderRadius, mBorderRadius, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        cWriter.drawRoundRect(bitmapRect, mBorderRadius, mBorderRadius, mBitmapPaint);

        return output;
    }

    private Bitmap drawDiagonal(int width, int height) {
        Path borderPath = createDiagonalPath(0, width, height);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        Path bitmapPath = createDiagonalPath(mBorderWidth, bitmapWidth, bitmapHeight);

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cWriter = new Canvas(output);
        cWriter.drawPath(borderPath, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        cWriter.drawPath(bitmapPath, mBitmapPaint);

        return output;
    }

    private Bitmap drawDiagonalInverted(int width, int height) {
        Path borderPath = createDiagonalPathInverted(0, width, height);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        Path bitmapPath = createDiagonalPathInverted(mBorderWidth, bitmapWidth, bitmapHeight);

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cWriter = new Canvas(output);
        cWriter.drawPath(borderPath, mBorderPaint);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        cWriter.drawPath(bitmapPath, mBitmapPaint);


        if (overlayBitmap != null) {

            int newWidth = overlayBitmap.getWidth();
            int newHeight = overlayBitmap.getHeight();

            BitmapShader shader = createBitmapShader(overlayBitmap, newWidth, newHeight);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            paint.setShader(shader);

            Path borderPath2 = createDiagonalPathInverted(0, width, height);

            Path bitmapPath2 = createDiagonalPathInverted(mBorderWidth, bitmapWidth, bitmapHeight);

            Bitmap finalBitmap2 = Bitmap.createScaledBitmap(overlayBitmap, newWidth, newHeight, true);
            Bitmap output2 = Bitmap.createBitmap(finalBitmap2.getWidth(), finalBitmap2.getHeight(), Bitmap.Config.ALPHA_8);

            Canvas cWriter2 = new Canvas(output2);
            cWriter2.drawPath(borderPath2, mBorderPaint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            cWriter2.drawBitmap(overlayBitmap, 0, 0, paint);
            paint.setXfermode(null);
            cWriter2.drawPath(bitmapPath2, paint);

            cWriter.drawBitmap(output2, 0, 0, paint);
        }

        return output;
    }

    private Bitmap drawDiagonalInverted(Bitmap bitmap, int width, int height) {
        Path borderPath = createDiagonalPathInverted(0, width, height);

        int bitmapWidth = width - mBorderWidth, bitmapHeight = height - mBorderWidth;
        Path bitmapPath = createDiagonalPathInverted(mBorderWidth, bitmapWidth, bitmapHeight);

        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cWriter = new Canvas(output);
        cWriter.drawPath(borderPath, mBorderPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cWriter.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        cWriter.drawPath(bitmapPath, mBitmapPaint);

        return output;
    }

    private Path createDiagonalPath(int start, int width, int height) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        int bottomRightPointY = (int) (0.75 * height);

        path.moveTo(start, start);
        path.lineTo(start, height);
        path.lineTo(width, bottomRightPointY);
        path.lineTo(width, start);
        path.lineTo(start, start);
        path.close();

        return path;
    }

    private Path createDiagonalPathInverted(int start, int width, int height) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        int bottomRightPointY = (int) (0.85 * height);

        path.moveTo(start, start);
        path.lineTo(start, bottomRightPointY);
        path.lineTo(width, height);
        path.lineTo(width, start);
        path.lineTo(start, start);
        path.close();

        return path;
    }

    private void drawStar(Canvas canvas, int width, int height) {
        int d = Math.min(width, height);
        int dw = width - d < 0 ? 0 : (width - d) / 2;
        int dh = height - d < 0 ? 0 : (height - d) / 2;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        int _d2 = d / 2, _d9 = d / 9, _5d18 = 5 * d / 18, _8d9 = 8 * d / 9, _7d18 = 7 * d / 18,
                _13d18 = 13 * d / 18, _11d18 = 11 * d / 18;

        path.moveTo(dw + _d2, dh + 0);
        path.lineTo(dw + _7d18, dh + _7d18);
        path.lineTo(dw + 0, dh + _7d18);
        path.lineTo(dw + _5d18, dh + _11d18);
        path.lineTo(dw + _d9, dh + d);
        path.lineTo(dw + _d2, dh + _13d18);
        path.lineTo(dw + _8d9, dh + d);
        path.lineTo(dw + _13d18, dh + _11d18);
        path.lineTo(dw + d, dh + _7d18);
        path.lineTo(dw + _11d18, dh + _7d18);
        path.lineTo(dw + _d2, dh + 0);
        path.close();

        canvas.drawPath(path, mBitmapPaint);
    }

    private void drawHeart(Canvas canvas, int width, int height) {
        int d = Math.min(width, height);
        int dw = width - d < 0 ? 0 : (width - d) / 2;
        int dh = height - d < 0 ? 0 : (height - d) / 2;
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        int _d2 = d / 2, _5d18 = 5 * d / 18, _d3 = d / 3, _2d9 = 2 * d / 9, _2d3 = 2 * d / 3, _7d9 = 7 * d / 9;

        path.moveTo(dw + _d2, dh + _5d18);
        path.cubicTo(dw + _d3, dh + 0, dw + 0, dh + _5d18, dw + _2d9, dh + _2d3);
        path.lineTo(dw + _d2, dh + d);
        path.lineTo(dw + _7d9, dh + _2d3);
        path.cubicTo(dw + d, dh + _5d18, dw + _2d3, dh + 0, dw + _d2, dh + _5d18);
        path.close();

        canvas.drawPath(path, mBitmapPaint);

    }

    private Bitmap applyFormInBitmap(int form) {
        return null;
    }

    public void setOverlayBitmap(Bitmap overlayBitmap) {
        this.overlayBitmap = overlayBitmap;
    }

    private static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

}
