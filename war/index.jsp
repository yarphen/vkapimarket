<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  <%= (request.getCookies()==null)?"<meta http-equiv='refresh' content='0;https://oauth.vk.com/authorize?client_id=5214831&display=popup&redirect_uri=https://vk-uploader.appspot.com/auth&scope=photos,market&response_type=code&v=5.42'>":"" %>
    
    <title>Hello App Engine</title>
  </head>

  <body>
    <h1>Hello App Engine!</h1>
	
    <table>
      <tr>
        <td colspan="2" style="font-weight:bold;">Available Servlets:</td>        
      </tr>
      <tr>
        <td><a href="https://oauth.vk.com/authorize?client_id=5214831&display=popup&redirect_uri=https://vk-uploader.appspot.com/auth&scope=photos,market&response_type=code&v=5.42">VKAuth</a></td>
      </tr>
      <tr>
        <td><a href="/u">VKUploader</a></td>
      </tr>
    </table>
    <form method="post" action="u">
   IMAGE URL:<br>
    <input type="text" name="image"/><br>
   GROUP ID:<br>
    <input type="text" name="group"/><br>
   NAME:<br>
    <input type="text" name="name"/><br>
   DESCRIPTION:<br>
    <input type="text" name="descr"/><br>
   CATEGORY:<br>
    <input type="text" name="category"/><br>
   PRICE:<br>
    <input type="text" name="price"/><br>
   CROP X:<br>
    <input type="text" name="crop_x"/><br>
   CROP Y:<br>
    <input type="text" name="crop_y"/><br>
   CROP WIDTH:<br>
    <input type="text" name="crop_width"/><br>
   STATUS:<br>
    <select>
    <option value="true">DISABLED</option>
    <option value="false">ENABLED</option>
    </select><br>
    <input type="submit" /><br>
    </form>
  </body>
</html>
