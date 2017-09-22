package com.example.zengqiang.mycloud.http.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zengqiang on 2017/8/26.
 */

public class ACache {
    private static final int MAX_HOUR=60*60;
    private static final int MAX_DAY=MAX_HOUR*24;
    private static final int MAX_SIZE=1000*1000*50;
    private static final int MAX_COUNT=Integer.MAX_VALUE;

    private static Map<String,ACache> mInstanceMap=new HashMap<>();
    private ACacheManage mCache;


    private ACache(File cacheDir,long maxSize,int maxCount){
        if(!cacheDir.exists()&&cacheDir.mkdirs()){
            throw new RuntimeException("无法创建路劲"+"ACache ");
        }
        Log.e("xys",cacheDir.toString());
        mCache=new ACacheManage(cacheDir,maxSize,maxCount);
    }

    public static ACache get(Context context){
        return get(context,"ACache");
    }

    public static ACache get(Context context,String name){
        File file=new File(context.getCacheDir(),name);
        return get(file,MAX_SIZE,MAX_COUNT);
    }

    public static ACache get(File file){
        return get(file,MAX_SIZE,MAX_COUNT);
    }

    public static ACache get(Context context,long maxSize,int maxCount){
        File file=new File(context.getCacheDir(),"ACache");
        return get(file,maxSize,maxCount);
    }

    public static ACache get(File cacheDir,long maxSize,int maxCount){
        ACache instance=mInstanceMap.get(cacheDir.getAbsolutePath()+myPid());
        if(instance==null){
            instance=new ACache(cacheDir,maxSize,maxCount);
            mInstanceMap.put(cacheDir.getAbsolutePath()+myPid(),instance);
        }
        return instance;
    }

    private static String myPid(){
        return "_"+android.os.Process.myPid();
    }

    public void put(String key,String value){
        File file=mCache.newFile(key);
        BufferedWriter writer=null;
        try {
            writer=new BufferedWriter(new FileWriter(file),1024);
            writer.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        mCache.put(file);
    }

    public void put(String key,String value,int time){
        put(key,Utils.newStringWithDataInfo(time,value));
    }

    public String getAsString(String key){
        File file=mCache.get(key);
        if(file==null){
            return null;
        }
        boolean isDue=false;
        BufferedReader reader=null;
        String str="";
        String current;
        try {
            reader=new BufferedReader(new FileReader(file));
            while((current=reader.readLine())!=null){
                str+=current;
            }
            if(!Utils.isDue(str)){
                return Utils.claerDataInfo(str);
            }
            else{
                isDue=true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isDue){
                remove(key);
            }
        }

    }

    public void put(String key, JSONObject object){
        put(key,object.toString());
    }

    public void put(String key,JSONObject object,int saveTime){
        put(key,object.toString(),saveTime);
    }

    public JSONObject getAsJSONObject(String key){
        String object=getAsString(key);
        try {
            JSONObject jsonObject=new JSONObject(object);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String key,JSONArray array){
        put(key,array.toString());
    }

    public void put(String key,JSONArray array,int saveTime){
        put(key,array.toString(),saveTime);
    }

    public JSONArray getAsJSONArray(String key){
        String str=getAsString(key);
        try {
            JSONArray array=new JSONArray(str);
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void put(String key,byte[] bytes){
        File file=mCache.newFile(key);
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mCache.put(file);
    }

    public void put(String key,byte[] bytes,int save){
        put(key,Utils.newByteWithDataInfo(save,bytes));
    }

    public byte[] getAsByte(String key){
        File file=mCache.get(key);
        if(file==null){
            return null;
        }
        boolean isDue=false;
        RandomAccessFile randomAccessFile=null;
        try {
            randomAccessFile=new RandomAccessFile(file,"r");
            byte[] bytes=new byte[(int)randomAccessFile.length()];
            randomAccessFile.read(bytes);
            if(!Utils.isDue(bytes)){
                return Utils.clearDateInfo(bytes);
            }
            else{
                isDue=true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(randomAccessFile!=null){
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isDue){
                remove(key);
            }
        }
    }

    public void put(String key,Serializable value){
        put(key,value,-1);
    }

    public void put(String key, Serializable value,int save){
        ByteArrayOutputStream byteArrayOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            byte[] bytes=byteArrayOutputStream.toByteArray();
            if(save==-1){
               put(key,bytes);
            }else{
                put(key,bytes,save);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(byteArrayOutputStream!=null){
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object getAsObject(String key){
        byte[] bytes=getAsByte(key);
        if(bytes!=null){
            ByteArrayInputStream byteArrayInputStream=null;
            ObjectInputStream objectInputStream=null;
            byteArrayInputStream=new ByteArrayInputStream(bytes);
            try {
                objectInputStream=new ObjectInputStream(byteArrayInputStream);
                Object object=objectInputStream.readObject();
                return object;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }finally {
                if(objectInputStream!=null){
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(byteArrayInputStream!=null){
                    try {
                        byteArrayInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public void put(String key,Bitmap value){
        put(key,Utils.bitmap2Byte(value));
    }

    public void put(String key,Bitmap value,int save){
        put(key,Utils.bitmap2Byte(value),save);
    }

    public void put(String key,Drawable value){
        put(key,Utils.drawable2Bitmap(value));
    }

    public void put(String key,Drawable value,int save){
        put(key,Utils.drawable2Bitmap(value),save);
    }

    public File file(String key){
        File file=mCache.newFile(key);
        if(file.exists()){
            return file;
        }
        return null;
    }

    public boolean remove(String key){
        return mCache.remove(key);
    }

    public class ACacheManage{
        private AtomicLong cacheSize;
        private AtomicInteger cacheCount;
        private long sizeLimit;
        private int countLimit;
        private final Map<File,Long> lastUsageDates= Collections.
                synchronizedMap(new HashMap<File, Long>());
        protected File cacheDir;

        private ACacheManage(File cacheDir,long maxSize,int maxCount){
            this.cacheDir=cacheDir;
            this.sizeLimit=maxSize;
            this.countLimit=maxCount;
            cacheSize=new AtomicLong();
            cacheCount=new AtomicInteger();
            caculateCacheSizeAndCacheCount();
        }

        private void caculateCacheSizeAndCacheCount(){
            new Thread(new Runnable() {
                int size=0;
                int count=0;
                @Override
                public void run() {
                    File[] files=cacheDir.listFiles();
                    if(files!=null){
                        for(File file:files){
                            size+=caculateFile(file);
                            count+=1;
                            lastUsageDates.put(file,file.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file){
            int count=cacheCount.get();
            while(count+1>countLimit){
                long fileLength=removeNext();
                cacheSize.addAndGet(-fileLength);
                count=cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);
            long curCacheSize=cacheSize.get();
            long fileLength=caculateFile(file);
            while(curCacheSize+fileLength>countLimit){
                long length=removeNext();
                curCacheSize=cacheSize.addAndGet(-length);
            }
            long currentTime=System.currentTimeMillis();
            cacheSize.addAndGet(fileLength);
            file.setLastModified(currentTime);
            lastUsageDates.put(file,currentTime);
        }

        private void clear(){
            cacheSize.set(0);
            lastUsageDates.clear();
            File[] files=cacheDir.listFiles();
            if(files!=null){
                for(File file:files){
                    file.delete();
                }
            }
        }

        private boolean remove(String key){
            File image=get(key);
            return image.delete();
        }

        private File get(String key){
            File file=newFile(key);
            long currentTime=System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file,currentTime);
            return file;
        }

        private File newFile(String key){
            return new File(cacheDir,key.hashCode()+"");
        }

        private long removeNext(){
            if(lastUsageDates==null){
                return 0;
            }
            File file=null;
            Long old=null;
            Set<Map.Entry<File,Long>> files=lastUsageDates.entrySet();
            synchronized (lastUsageDates){
                if(files!=null){
                    for(Map.Entry file1:files){
                        if(file==null){
                            file=(File)file1.getKey();
                            old=(Long)file1.getValue();
                        }
                        else{
                            Long a=(Long)file1.getValue();
                            if(a<old){
                                old=a;
                                file=(File)file1.getKey();
                            }
                        }
                    }
                }
            }
            long length=file.length();
            if(file.delete()){
                lastUsageDates.remove(file);

            }
            return length;
        }

        private long caculateFile(File file){
            return file.length();
        }

    }

    private static class Utils{

        private static char mSeparator=' ';

        private static String newStringWithDataInfo(int second,String info){
            return createDataInfo(second)+info;
        }

        private static byte[] newByteWithDataInfo(int second,byte[] info){
            byte[] a=createDataInfo(second).getBytes();
            byte[] aim=new byte[a.length+info.length];
            System.arraycopy(a,0,aim,0,a.length);
            System.arraycopy(info,0,aim,a.length,info.length);
            return aim;
        }

        private static String claerDataInfo(String info){
            if(info!=null&&hasDataInfo(info.getBytes())){
                return info.substring(info.indexOf(mSeparator)+1,info.length());
            }
            return info;
        }

        private static byte[] clearDateInfo(byte[] info){
            if(info!=null&&hasDataInfo(info)){
                return copyOfRange(info,indexOf(info,mSeparator)+1,info.length);
            }
            return info;
        }

        private static String createDataInfo(int second){
            String s=System.currentTimeMillis()+"";
            while(s.length()<13){
                s="0"+s;
            }
            s=s+"-"+second+mSeparator;
            return s;
        }

        private static byte[] bitmap2Byte(Bitmap bitmap){
            if(bitmap==null){
                return null;
            }
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            return outputStream.toByteArray();
        }

        private static Bitmap byte2Bitmap(byte[] bytes){
            if(bytes==null){
                return null;
            }
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }

        private static Bitmap drawable2Bitmap(Drawable drawable){
            if(drawable==null){
                return null;
            }
            int w=drawable.getIntrinsicWidth();
            int h=drawable.getIntrinsicHeight();
            Bitmap.Config config=drawable.getOpacity()!= PixelFormat.OPAQUE?
                    Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
            Bitmap bitmap=Bitmap.createBitmap(w,h,config);
            Canvas canvas=new Canvas(bitmap);
            drawable.setBounds(0,0,w,h);
            drawable.draw(canvas);
            return bitmap;
        }

        public static boolean isDue(String str){
            return isDue(str.getBytes());
        }

        private static boolean isDue(byte[] data){
            String[] str=getDataInfoFromData(data);
            if(str!=null){
                String save=str[0];
                while(save.startsWith("0")){
                    save=save.substring(1,save.length());
                }
                long saveTime=Long.parseLong(save);
                long deleteTime=Long.parseLong(str[1]);
                if(System.currentTimeMillis()>saveTime+deleteTime*1000){
                    return true;
                }
            }
            return false;
        }

        private static boolean hasDataInfo(byte[] data){
            if(data!=null&&data.length>154&&data[13]=='-'
                    &&indexOf(data,mSeparator)>14){
                return true;
            }
            return false;
        }

        private static String[] getDataInfoFromData(byte[] data){
            if(hasDataInfo(data)){
                String save=new String(copyOfRange(data,0,13));
                String delete=new String(copyOfRange(data,14,indexOf(data,mSeparator)));
                return new String[]{
                        save,delete
                };
            }
            return null;
        }

        private static Drawable bitmap2Drawable(Bitmap bitmap){
            if(bitmap==null){
                return null;
            }
            return new BitmapDrawable(bitmap);
        }

        private static byte[] copyOfRange(byte[] data,int from,int to){
            int newLength=to-from;
            if(newLength<0){
                throw new IllegalArgumentException("ACache copyOfRange");
            }
            byte[] copy=new byte[newLength];
            System.arraycopy(data,from,copy,0,Math.min(data.length-from,newLength));
            return copy;
        }

        private static int indexOf(byte[] data,char c){
            if(data!=null){
                for(int i=0;i<data.length;i++){
                    if(data[i]==c){
                        return i;
                    }
                }
            }
            return -1;
        }
    }
}
