'*******************************************************************************
'* File:           EasyVBLdap.vbs
'* 
'* Purpose:        EASY IMPLEMENTATION TO GET EMAIL FROM LDAP
'* Version:        1.0 (24 May 2012)
'*
'* 
'* Author:		   Jon Normington
'* Requirements:   > Windows XP
'*                 Windows Script Host 5.6 - CSCRIPT.EXE_OR_WSCRIPT.EXE
'*******************************************************************************
Option Explicit
On Error Resume Next

readInputArgsAndExe

Function readInputArgsAndExe
	If WScript.Arguments.Count < 3 then
		WScript.Echo "Usage: EasyVBLdap.vbs ldapURL firstName lastName"
		WScript.Echo "Example: EasyVBLdap LDAP://dc=domain,dc=co,dc=uk John Smith"
		WScript.Quit
	End If

	WScript.Echo "mail:" & searchQuery(WScript.Arguments.Item(0), WScript.Arguments.Item(1), WScript.Arguments.Item(2))
End Function


Function searchQuery(ldapURL, firstName, lastName)
	Dim objConnection, objCommand, objRecordSet, mailToReturn	
	Set objConnection = CreateObject("ADODB.Connection") 
	Set objCommand =   CreateObject("ADODB.Command") 
	objConnection.Provider = "ADsDSOObject" 
	objConnection.Open "Active Directory Provider" 
	Set objCommand.ActiveConnection = objConnection
	Err.Clear
	
	'SQL statement to query AD replace strNTUserName with 
	'the user that is executing this script upon logon
	objCommand.CommandText = _ 
	    "SELECT givenName, sn, mail " &_
	    "FROM '" & ldapURL & "' " &_
	    "WHERE objectCategory='user' " &_
	    "AND sn = '" & lastName & "'" &_
	    "AND givenName = '" & firstName & "'"
	
	Set objRecordSet = objCommand.Execute
	
	If IsNull(objRecordSet) Or objRecordSet.BOF Or objRecordSet.EOF _
		Or objRecordSet.RecordCount > 1 Then
		searchQuery = ""
	Else
		searchQuery = objRecordSet.Fields("mail")
	End If			
	
	objConnection.Close
End Function
