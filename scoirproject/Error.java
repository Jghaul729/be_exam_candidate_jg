/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scoirproject;

/**
 *
 * @author jaghaul
 */
public class Error {
    private String _message;
    private int _lineNum;
    public Error(String message, int lineNum)
    {
        _message = message;
        _lineNum = lineNum;
    }
    public String getMessage()
    {
        return _message;
    }
    public int getLineNum()
    {
        return _lineNum;
    }
}
