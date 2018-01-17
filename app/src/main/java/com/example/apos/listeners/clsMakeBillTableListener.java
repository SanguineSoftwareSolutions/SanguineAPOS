package com.example.apos.listeners;


import android.app.Activity;
import android.content.Context;

import com.example.apos.fragments.clsMakeBillLoadTable;

import java.util.ArrayList;
import java.util.List;

public interface clsMakeBillTableListener
{
    void funGetSelectedTable(String tableNo,String tableName);
}