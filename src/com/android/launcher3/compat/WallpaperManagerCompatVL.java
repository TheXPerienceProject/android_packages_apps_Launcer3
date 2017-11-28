// 
// Decompiled by Procyon v0.5.30
// 

package com.android.launcher3.compat;

import android.app.WallpaperManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;

import com.android.launcher3.Utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class WallpaperManagerCompatVL extends WallpaperManagerCompat
{
    private static final String ACTION_EXTRACTION_COMPLETE = "com.android.launcher3.compat.WallpaperManagerCompatVL.EXTRACTION_COMPLETE";
    private static final String KEY_COLORS = "wallpaper_parsed_colors";
    private static final String TAG = "WMCompatVL";
    private static final String VERSION_PREFIX = "1,";
    private WallpaperColorsCompat mColorsCompat;
    private final Context mContext;
    private final ArrayList<OnColorsChangedListenerCompat> mListeners;
    
    WallpaperManagerCompatVL(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: iconst_m1      
        //     3: istore_3       
        //     4: aload_0        
        //     5: invokespecial   com/android/launcher3/compat/WallpaperManagerCompat.<init>:()V
        //     8: new             Ljava/util/ArrayList;
        //    11: astore          4
        //    13: aload           4
        //    15: invokespecial   java/util/ArrayList.<init>:()V
        //    18: aload_0        
        //    19: aload           4
        //    21: putfield        com/android/launcher3/compat/WallpaperManagerCompatVL.mListeners:Ljava/util/ArrayList;
        //    24: aload_0        
        //    25: aload_1        
        //    26: putfield        com/android/launcher3/compat/WallpaperManagerCompatVL.mContext:Landroid/content/Context;
        //    29: aload_0        
        //    30: getfield        com/android/launcher3/compat/WallpaperManagerCompatVL.mContext:Landroid/content/Context;
        //    33: invokestatic    com/android/launcher3/Utilities.getDevicePrefs:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //    36: astore          4
        //    38: ldc             ""
        //    40: astore          5
        //    42: aload           4
        //    44: ldc             "wallpaper_parsed_colors"
        //    46: aload           5
        //    48: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    53: astore          4
        //    55: ldc             "1,"
        //    57: astore          6
        //    59: aload           4
        //    61: aload           6
        //    63: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    66: istore          7
        //    68: iload           7
        //    70: ifeq            357
        //    73: aload           4
        //    75: invokestatic    com/android/launcher3/compat/WallpaperManagerCompatVL.parseValue:(Ljava/lang/String;)Landroid/util/Pair;
        //    78: astore          5
        //    80: aload           5
        //    82: getfield        android/util/Pair.first:Ljava/lang/Object;
        //    85: checkcast       Ljava/lang/Integer;
        //    88: invokevirtual   java/lang/Integer.intValue:()I
        //    91: istore          7
        //    93: aload           5
        //    95: getfield        android/util/Pair.second:Ljava/lang/Object;
        //    98: checkcast       Lcom/android/launcher3/compat/WallpaperColorsCompat;
        //   101: astore          4
        //   103: aload_0        
        //   104: aload           4
        //   106: putfield        com/android/launcher3/compat/WallpaperManagerCompatVL.mColorsCompat:Lcom/android/launcher3/compat/WallpaperColorsCompat;
        //   109: iload           7
        //   111: istore          8
        //   113: iload           8
        //   115: iload_3        
        //   116: if_icmpeq       132
        //   119: aload_1        
        //   120: invokestatic    com/android/launcher3/compat/WallpaperManagerCompatVL.getWallpaperId:(Landroid/content/Context;)I
        //   123: istore          7
        //   125: iload           8
        //   127: iload           7
        //   129: if_icmpeq       136
        //   132: aload_0        
        //   133: invokespecial   com/android/launcher3/compat/WallpaperManagerCompatVL.reloadColors:()V
        //   136: new             Lcom/android/launcher3/compat/WallpaperManagerCompatVL$1;
        //   139: astore          4
        //   141: aload           4
        //   143: aload_0        
        //   144: invokespecial   com/android/launcher3/compat/WallpaperManagerCompatVL$1.<init>:(Lcom/android/launcher3/compat/WallpaperManagerCompatVL;)V
        //   147: new             Landroid/content/IntentFilter;
        //   150: astore          6
        //   152: ldc             "android.intent.action.WALLPAPER_CHANGED"
        //   154: astore          9
        //   156: aload           6
        //   158: aload           9
        //   160: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //   163: aload_1        
        //   164: aload           4
        //   166: aload           6
        //   168: invokevirtual   android/content/Context.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
        //   171: pop            
        //   172: iconst_0       
        //   173: istore          7
        //   175: aconst_null    
        //   176: astore          6
        //   178: aload_1        
        //   179: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //   182: astore          4
        //   184: aload_1        
        //   185: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //   188: astore          9
        //   190: sipush          4096
        //   193: istore          10
        //   195: aload           4
        //   197: aload           9
        //   199: iload           10
        //   201: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   204: astore          4
        //   206: aload           4
        //   208: getfield        android/content/pm/PackageInfo.permissions:[Landroid/content/pm/PermissionInfo;
        //   211: astore          9
        //   213: aload           9
        //   215: arraylength    
        //   216: istore          10
        //   218: iconst_0       
        //   219: istore          8
        //   221: aconst_null    
        //   222: astore          4
        //   224: iconst_0       
        //   225: istore          7
        //   227: aconst_null    
        //   228: astore          6
        //   230: iload           7
        //   232: iload           10
        //   234: if_icmpge       293
        //   237: aload           9
        //   239: iload           7
        //   241: aaload         
        //   242: astore_2       
        //   243: aload_2        
        //   244: getfield        android/content/pm/PermissionInfo.protectionLevel:I
        //   247: iconst_2       
        //   248: iand           
        //   249: istore          11
        //   251: iload           11
        //   253: ifeq            262
        //   256: aload_2        
        //   257: getfield        android/content/pm/PermissionInfo.name:Ljava/lang/String;
        //   260: astore          4
        //   262: iload           7
        //   264: iconst_1       
        //   265: iadd           
        //   266: istore          7
        //   268: goto            230
        //   271: astore          4
        //   273: ldc             "WMCompatVL"
        //   275: astore_2       
        //   276: ldc             "Unable to get permission info"
        //   278: astore          9
        //   280: aload_2        
        //   281: aload           9
        //   283: aload           4
        //   285: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   288: pop            
        //   289: aload           6
        //   291: astore          4
        //   293: aload_0        
        //   294: getfield        com/android/launcher3/compat/WallpaperManagerCompatVL.mContext:Landroid/content/Context;
        //   297: astore          6
        //   299: new             Lcom/android/launcher3/compat/WallpaperManagerCompatVL$2;
        //   302: astore_2       
        //   303: aload_2        
        //   304: aload_0        
        //   305: invokespecial   com/android/launcher3/compat/WallpaperManagerCompatVL$2.<init>:(Lcom/android/launcher3/compat/WallpaperManagerCompatVL;)V
        //   308: new             Landroid/content/IntentFilter;
        //   311: astore          9
        //   313: aload           9
        //   315: ldc             "com.android.launcher3.compat.WallpaperManagerCompatVL.EXTRACTION_COMPLETE"
        //   317: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //   320: new             Landroid/os/Handler;
        //   323: astore          5
        //   325: aload           5
        //   327: invokespecial   android/os/Handler.<init>:()V
        //   330: aload           6
        //   332: aload_2        
        //   333: aload           9
        //   335: aload           4
        //   337: aload           5
        //   339: invokevirtual   android/content/Context.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;Ljava/lang/String;Landroid/os/Handler;)Landroid/content/Intent;
        //   342: pop            
        //   343: return         
        //   344: astore          12
        //   346: aload           4
        //   348: astore          6
        //   350: aload           12
        //   352: astore          4
        //   354: goto            273
        //   357: iload_3        
        //   358: istore          8
        //   360: goto            113
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  178    182    271    273    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  184    188    271    273    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  199    204    271    273    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  206    211    271    273    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  213    216    271    273    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  239    242    344    357    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  243    247    344    357    Landroid/content/pm/PackageManager$NameNotFoundException;
        //  256    260    344    357    Landroid/content/pm/PackageManager$NameNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0262:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static final int getWallpaperId(final Context context) {
        if (!Utilities.ATLEAST_NOUGAT) {
            return -1;
        }
        return ((WallpaperManager)context.getSystemService((Class)WallpaperManager.class)).getWallpaperId(WallpaperManager.FLAG_SYSTEM);
    }
    
    private void handleResult(final String s) {
        Utilities.getDevicePrefs(this.mContext).edit().putString("wallpaper_parsed_colors", s).apply();
        this.mColorsCompat = (WallpaperColorsCompat)parseValue(s).second;
        final Iterator iterator = this.mListeners.iterator();
        for (OnColorsChangedListenerCompat l : mListeners) {
            l.onColorsChanged(this.mColorsCompat, 1);
        }
    }
    
    private static Pair parseValue(final String s) {
        final int n = 4;
        final int n2 = 3;
        final int n3 = 2;
        final String[] split = s.split(",");
        final Integer value = Integer.parseInt(split[1]);
        if (split.length == n3) {
            return Pair.create((Object)value, (Object)null);
        }
        int int1;
        if (split.length > n3) {
            int1 = Integer.parseInt(split[n3]);
        }
        else {
            int1 = 0;
        }
        int int2;
        if (split.length > n2) {
            int2 = Integer.parseInt(split[n2]);
        }
        else {
            int2 = 0;
        }
        int int3;
        if (split.length > n) {
            int3 = Integer.parseInt(split[n]);
        }
        else {
            int3 = 0;
        }
        return Pair.create((Object)value, (Object)new WallpaperColorsCompat(int1, int2, int3, 0));
    }
    
    private void reloadColors() {
        ((JobScheduler)this.mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)).schedule(new JobInfo.Builder(2, new ComponentName(this.mContext, WallpaperManagerCompatVL$ColorExtractionService.class)).setMinimumLatency(0L).build());
    }
    
    public void addOnColorsChangedListener(final OnColorsChangedListenerCompat wallpaperManagerCompatOnColorsChangedListenerCompat) {
        this.mListeners.add(wallpaperManagerCompatOnColorsChangedListenerCompat);
    }
    
    public WallpaperColorsCompat getWallpaperColors(final int n) {
        WallpaperColorsCompat mColorsCompat;
        if (n == 1) {
            mColorsCompat = this.mColorsCompat;
        }
        else {
            mColorsCompat = null;
        }
        return mColorsCompat;
    }

    final class WallpaperManagerCompatVL$1 extends BroadcastReceiver
    {
        final /* synthetic */ WallpaperManagerCompatVL this$0;

        WallpaperManagerCompatVL$1(final WallpaperManagerCompatVL this$0) {
            this.this$0 = this$0;
        }

        public void onReceive(final Context context, final Intent intent) {
            this.this$0.reloadColors();
        }
    }

    final class WallpaperManagerCompatVL$2 extends BroadcastReceiver
    {
        final /* synthetic */ WallpaperManagerCompatVL this$0;

        WallpaperManagerCompatVL$2(final WallpaperManagerCompatVL this$0) {
            this.this$0 = this$0;
        }

        public void onReceive(final Context context, final Intent intent) {
            this.this$0.handleResult(intent.getStringExtra("wallpaper_parsed_colors"));
        }
    }

    public class WallpaperManagerCompatVL$ColorExtractionService extends JobService implements Runnable
    {
        private static final int MAX_WALLPAPER_EXTRACTION_AREA = 12544;
        private Handler mWorkerHandler;
        private HandlerThread mWorkerThread;

        public void onCreate() {
            super.onCreate();
            (this.mWorkerThread = new HandlerThread("ColorExtractionService")).start();
            this.mWorkerHandler = new Handler(this.mWorkerThread.getLooper());
        }

        public void onDestroy() {
            super.onDestroy();
            this.mWorkerThread.quit();
        }

        public boolean onStartJob(final JobParameters jobParameters) {
            this.mWorkerHandler.post((Runnable)this);
            return true;
        }

        public boolean onStopJob(final JobParameters jobParameters) {
            this.mWorkerHandler.removeCallbacksAndMessages((Object)null);
            return true;
        }

        public void run() {
            /*final int -wrap0 = getWallpaperId((Context)this);
            Object o = null;
            final WallpaperManager instance = WallpaperManager.getInstance((Context)this);
            final WallpaperInfo wallpaperInfo = instance.getWallpaperInfo();
            String string2 = null;
            Label_0848: {
                final StringBuilder sb;
                final ArrayList<Pair> list;
                Label_0740: {
                    Object bitmap = null;
                    Label_0057: {
                        if (wallpaperInfo != null) {
                            final Drawable loadThumbnail = wallpaperInfo.loadThumbnail(this.getPackageManager());
                            bitmap = null;
                            o = loadThumbnail;
                        }
                        else {
                            Label_0896: {
                                Label_0647: {
                                    if (!Utilities.ATLEAST_NOUGAT) {
                                        break Label_0647;
                                    }
                                    final Object o2 = null;
                                    Object wallpaperFile = null;
                                    final int n = 1;
                                    final WallpaperManager wallpaperManager = instance;
                                    try {
                                        wallpaperFile = wallpaperManager.getWallpaperFile(n);
                                        final BitmapRegionDecoder instance2 = BitmapRegionDecoder.newInstance(((ParcelFileDescriptor)wallpaperFile).getFileDescriptor(), false);
                                        final int n2 = instance2.getWidth() * instance2.getHeight();
                                        final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
                                        if (n2 > 12544) {
                                            bitmapFactory$Options.inSampleSize = (int)Math.pow(2.0, Math.floor(Math.log(n2 / 12544.0) / (Math.log(2.0) * 2.0)));
                                        }
                                        o = instance2.decodeRegion(new Rect(0, 0, instance2.getWidth(), instance2.getHeight()), bitmapFactory$Options);
                                        instance2.recycle();
                                        Label_0615: {
                                            if (wallpaperFile == null) {
                                                break Label_0615;
                                            }
                                            try {
                                                ((ParcelFileDescriptor)wallpaperFile).close();
                                                if (o2 != null) {
                                                    ParcelFileDescriptor parcelFileDescriptor;
                                                    try {
                                                        throw o2;
                                                    }
                                                    catch (IOException | NullPointerException ex) {
                                                        final Bitmap bitmap2;
                                                        bitmap = bitmap2;
                                                        parcelFileDescriptor = (ParcelFileDescriptor)o;
                                                    }
                                                    wallpaperFile = "WMCompatVL";
                                                    Log.e((String)wallpaperFile, "Fetching partial bitmap failed, trying old method", (Throwable)bitmap);
                                                    o = parcelFileDescriptor;
                                                }
                                                if (o == null) {
                                                    o = instance.getDrawable();
                                                    break Label_0057;
                                                }
                                                break Label_0896;
                                            }
                                            finally {}
                                        }
                                    }
                                    finally {
                                        try {}
                                        finally {
                                            final ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)o;
                                            final String s;
                                            o = s;
                                            final Bitmap bitmap3;
                                            bitmap = bitmap3;
                                            while (true) {
                                                if (wallpaperFile == null) {
                                                    break Label_0699;
                                                }
                                                try {
                                                    ((ParcelFileDescriptor)wallpaperFile).close();
                                                    if (o != null) {}
                                                }
                                                finally {
                                                    if (o == null) {
                                                        o = wallpaperFile;
                                                        continue;
                                                    }
                                                    if (o != wallpaperFile) {
                                                        ((Throwable)o).addSuppressed((Throwable)wallpaperFile);
                                                    }
                                                    continue;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                break Label_0740;
                            }
                            bitmap = o;
                            o = null;
                        }
                    }
                    if (o != null) {
                        final int n3 = ((Drawable)o).getIntrinsicWidth() * ((Drawable)o).getIntrinsicHeight();
                        double sqrt = 1.0;
                        if (n3 > 12544) {
                            sqrt = Math.sqrt(12544.0 / n3);
                        }
                        bitmap = Bitmap.createBitmap((int)(((Drawable)o).getIntrinsicWidth() * sqrt), (int)(sqrt * ((Drawable)o).getIntrinsicHeight()), Bitmap.Config.ARGB_8888);
                        final Canvas canvas = new Canvas((Bitmap)bitmap);
                        ((Drawable)o).setBounds(0, 0, ((Bitmap)bitmap).getWidth(), ((Bitmap)bitmap).getHeight());
                        ((Drawable)o).draw(canvas);
                    }
                    final String string = "1," + -wrap0;
                    if (bitmap == null) {
                        string2 = string;
                        break Label_0848;
                    }
                    final a adH = a.adu((Bitmap)bitmap).adH();
                    ((Bitmap)bitmap).recycle();
                    sb = new StringBuilder(string);
                    list = new ArrayList<>();
                    for (final d d : adH.adr()) {
                        list.add(new Pair((Object)d.adM(), (Object)d.adK()));
                    }
                }
                Collections.sort(list, new WallpaperManagerCompatVL$ColorExtractionService$1(this));
                for (int i = 0; i < Math.min(3, list.size()); ++i) {
                    sb.append(',').append(list.get(i).first);
                }
                string2 = sb.toString();
            }
            this.sendBroadcast(new Intent("com.android.launcher3.compat.WallpaperManagerCompatVL.EXTRACTION_COMPLETE").setPackage(this.getPackageName()).putExtra("wallpaper_parsed_colors", string2));
            */
        }

        final class WallpaperManagerCompatVL$ColorExtractionService$1 implements Comparator<Pair>
        {
            final /* synthetic */ WallpaperManagerCompatVL$ColorExtractionService this$1;

            WallpaperManagerCompatVL$ColorExtractionService$1(final WallpaperManagerCompatVL$ColorExtractionService this$1) {
                this.this$1 = this$1;
            }

            public int compare(final Pair pair, final Pair pair2) {
                return (int)pair2.second - (int)pair.second;
            }
        }
    }
}
