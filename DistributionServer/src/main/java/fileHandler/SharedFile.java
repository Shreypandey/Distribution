package fileHandler;

import constants.ResponseCode;
import data.File;
import mainClasses.Main;
import request.Response;
import request.SharedFileRequest;
import searchHandler.FileGetter;
import tools.SharedFileResponse;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SharedFile
{
    private SharedFileRequest sharedFileRequest;
    private String userUID;
    List<File> files;
    List<Integer> noOfDownloads;
    public SharedFile(SharedFileRequest sharedFileRequest)
    {
        this.sharedFileRequest=sharedFileRequest;
        this.userUID=sharedFileRequest.getUserUID();
    }
    public Response getResponse()
    {
        userUID=sharedFileRequest.getUserUID();
        String query="SELECT fileUID from UserHistory WHERE userUID=\""+userUID+"\" and shared=\"1\";";
        try
        {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            ResultSet rs=preparedStatement.executeQuery();
            files=new ArrayList<File>();
            while (rs.next())
            {
                files.add(FileGetter.getFileByUID(rs.getString("fileUID")));
            }
            SharedFileResponse sharedFileResponse=new SharedFileResponse(files,noOfDownloads);
            sharedFileResponse.setValues();
            return new Response(UIDGenerator.generateuid(), sharedFileResponse, ResponseCode.SUCCESS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new Response(UIDGenerator.generateuid(), null, ResponseCode.FAILED);
        }
    }
}
