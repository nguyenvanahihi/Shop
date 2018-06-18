package com.example.alviss.ungdungquanlyshop.base;

import com.example.alviss.ungdungquanlyshop.models.HangHoa;
import com.example.alviss.ungdungquanlyshop.models.HoaDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.alviss.ungdungquanlyshop.fragment_bill.arrHoaDon;
import static com.example.alviss.ungdungquanlyshop.fragment_mer.arrHangHoa;
import static com.example.alviss.ungdungquanlyshop.fragment_per.arrKhachHang;

public class FirebaseFunctions {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference HangHoa = firebaseDatabase.getReference("HangHoa");
    public DatabaseReference KhachHang = firebaseDatabase.getReference("KhachHang");
    public DatabaseReference HoaDon = firebaseDatabase.getReference("HoaDon");
    public DatabaseReference ChiTietHoaDon = firebaseDatabase.getReference("ChiTietHoaDon");

    public void getHangHoa(){

        HangHoa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    arrHangHoa.add(postSnapshot.getValue(com.example.alviss.ungdungquanlyshop.models.HangHoa.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getKhachHang(){

        KhachHang.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    arrKhachHang.add(postSnapshot.getValue(com.example.alviss.ungdungquanlyshop.models.KhachHang.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getHoaDon(){

        HoaDon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                   // arrayList.add(postSnapshot);
                    arrHoaDon.add(postSnapshot.getValue(com.example.alviss.ungdungquanlyshop.models.HoaDon.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getChiTietHoaDon(){

        HoaDon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    // arrayList.add(postSnapshot);
                    arrHoaDon.add(postSnapshot.getValue(com.example.alviss.ungdungquanlyshop.models.HoaDon.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
