package com.bignerdranch.android.gridviewwithpictures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;


class Custom2 {
    List<Integer> first;
    List<Integer> second;
}

public class PictRes {
    private static PictRes sPictRes;
    private List<Integer> mRid;


    public static PictRes instance() {
        if (sPictRes == null) {
            sPictRes = new PictRes();
        }
        return sPictRes;
    }

    public int size() {
        return mRid.size();
    }


    public List<Integer> getArr() {
        return new ArrayList<>(mRid);
    }

    public List<Integer> getArr(int sz) {
        List<Integer> retList = new ArrayList<>();
        for (int i = 0; i < sz; i++) {
            retList.add(mRid.get(i));
        }
        return retList;
    }

     public List<Integer>getArrOfRepeatedElement(int k, int sz) {
         List<Integer> retList = new ArrayList<>();
         for (int i = 0; i < sz; i++) {
             retList.add(k);
         }
         return retList;
     }

    public Custom2 getTwoCustomArrays(int sz) {
    /* Функция возвращает в классе Custom2 два ArrayList-массива,
        заполненные элементами mRid так,
        что элементы выбираются рандомно и все отобранные элементы уникальны;
        sz - размер каждого из двух массивов возвращаемого класса
        и оно же число картинок каждого gridview
    */
        int size = sz * 2;
        Set<Integer> customSet = new LinkedHashSet<>();
        Random rand = new Random(new Date().getTime());
        while (customSet.size() <= size) {
            int nextInt = rand.nextInt(mRid.size());
            customSet.add(mRid.get(nextInt));
        }
        Custom2 ret = new Custom2();
        ret.first = new ArrayList<>();
        ret.second = new ArrayList<>();

        Iterator iter = customSet.iterator();
        for (int i = 0; i < sz; i++) {
            ret.first.add((int)iter.next());
        }

        for (int i = 0; i < sz; i++) {
            ret.second.add((int)iter.next());
        }

        return ret;
    }

    private PictRes() {
        mRid = new ArrayList<>();
        Integer[] arr = {
                R.drawable.pict1,   R.drawable.pict2,   R.drawable.pict3,   R.drawable.pict4,   R.drawable.pict5,
                R.drawable.pict6,   R.drawable.pict7,   R.drawable.pict8,   R.drawable.pict9,   R.drawable.pict10,
                R.drawable.pict11,  R.drawable.pict12,  R.drawable.pict13,  R.drawable.pict14,  R.drawable.pict15,
                R.drawable.pict16,  R.drawable.pict17,  R.drawable.pict18,  R.drawable.pict19,  R.drawable.pict20,
                R.drawable.pict21,  R.drawable.pict22,  R.drawable.pict23,  R.drawable.pict24,  R.drawable.pict25,
                R.drawable.pict26,  R.drawable.pict27,  R.drawable.pict28,  R.drawable.pict29,  R.drawable.pict30,
                R.drawable.pict31,  R.drawable.pict32,  R.drawable.pict33,  R.drawable.pict34,  R.drawable.pict35,
                R.drawable.pict36,  R.drawable.pict37,  R.drawable.pict38,  R.drawable.pict39,  R.drawable.pict40,
                R.drawable.pict41,  R.drawable.pict42,  R.drawable.pict43,  R.drawable.pict44,  R.drawable.pict45,
                R.drawable.pict46,  R.drawable.pict47,  R.drawable.pict48,  R.drawable.pict49,  R.drawable.pict50,
                R.drawable.pict51,  R.drawable.pict52,  R.drawable.pict53,  R.drawable.pict54,  R.drawable.pict55,
                R.drawable.pict56,  R.drawable.pict57,  R.drawable.pict58,  R.drawable.pict59,  R.drawable.pict60,
                R.drawable.pict61,  R.drawable.pict62,  R.drawable.pict63,  R.drawable.pict64,  R.drawable.pict65,
                R.drawable.pict66,  R.drawable.pict67,  R.drawable.pict68,  R.drawable.pict69,  R.drawable.pict70,
                R.drawable.pict71,  R.drawable.pict72,  R.drawable.pict73,  R.drawable.pict74,  R.drawable.pict75,
                R.drawable.pict76,  R.drawable.pict77,  R.drawable.pict78,  R.drawable.pict79,  R.drawable.pict80,
                R.drawable.pict81,  R.drawable.pict82,  R.drawable.pict83,  R.drawable.pict84,  R.drawable.pict85,
                R.drawable.pict86,  R.drawable.pict87,  R.drawable.pict88,  R.drawable.pict89,  R.drawable.pict90,
                R.drawable.pict91,  R.drawable.pict92,  R.drawable.pict93,  R.drawable.pict94,  R.drawable.pict95,
                R.drawable.pict96,  R.drawable.pict97,  R.drawable.pict98,  R.drawable.pict99,  R.drawable.pict100,
                R.drawable.pict101, R.drawable.pict102, R.drawable.pict103, R.drawable.pict104, R.drawable.pict105,
                R.drawable.pict106, R.drawable.pict107, R.drawable.pict108, R.drawable.pict109, R.drawable.pict110,
                R.drawable.pict111, R.drawable.pict112, R.drawable.pict113, R.drawable.pict114, R.drawable.pict115,
                R.drawable.pict116, R.drawable.pict117, R.drawable.pict118, R.drawable.pict119, R.drawable.pict120,
                R.drawable.pict121, R.drawable.pict122, R.drawable.pict123, R.drawable.pict124, R.drawable.pict125,
                R.drawable.pict126, R.drawable.pict127, R.drawable.pict128, R.drawable.pict129, R.drawable.pict130,
                R.drawable.pict131, R.drawable.pict132, R.drawable.pict133, R.drawable.pict134, R.drawable.pict135,
                R.drawable.pict136, R.drawable.pict137, R.drawable.pict138, R.drawable.pict139, R.drawable.pict140,
                R.drawable.pict141, R.drawable.pict142, R.drawable.pict143, R.drawable.pict144, R.drawable.pict145,
                R.drawable.pict146, R.drawable.pict147, R.drawable.pict148, R.drawable.pict149, R.drawable.pict150,
                R.drawable.pict151, R.drawable.pict152, R.drawable.pict153, R.drawable.pict154, R.drawable.pict155,
                R.drawable.pict156, R.drawable.pict157, R.drawable.pict158, R.drawable.pict159, R.drawable.pict160,
                R.drawable.pict161, R.drawable.pict162, R.drawable.pict163, R.drawable.pict164, R.drawable.pict165,
                R.drawable.pict166, R.drawable.pict167, R.drawable.pict168, R.drawable.pict169, R.drawable.pict170,
                R.drawable.pict171, R.drawable.pict172, R.drawable.pict173, R.drawable.pict174, R.drawable.pict175,
                R.drawable.pict176, R.drawable.pict177, R.drawable.pict178, R.drawable.pict179, R.drawable.pict180,
                R.drawable.pict181, R.drawable.pict182, R.drawable.pict183, R.drawable.pict184, R.drawable.pict185,
                R.drawable.pict186, R.drawable.pict187, R.drawable.pict188, R.drawable.pict189, R.drawable.pict190,
                R.drawable.pict191, R.drawable.pict192, R.drawable.pict193, R.drawable.pict194, R.drawable.pict195,
                R.drawable.pict196, R.drawable.pict197, R.drawable.pict198, R.drawable.pict199, R.drawable.pict200,
                R.drawable.pict201, R.drawable.pict202, R.drawable.pict203, R.drawable.pict204, R.drawable.pict205
        };
        Collections.addAll(mRid, arr);
    }
}
