DELIMITER $$  
  
USE `robot`$$  
  
DROP FUNCTION IF EXISTS `getRand`$$  
  
CREATE FUNCTION `getRand`(counts INTEGER) RETURNS VARCHAR(20) CHARSET utf8  
BEGIN  
       DECLARE sTemp VARCHAR(20);  
       DECLARE sTempCounts INTEGER;  
       SET sTemp = CONCAT( ROUND(ROUND(RAND(),counts)*(POW(10,counts))),'');  
         
       IF(CHAR_LENGTH(sTemp)<counts) THEN  
         
         SET sTempCounts = counts - CHAR_LENGTH(sTemp);  
         SET sTemp = CONCAT(sTemp, RIGHT(CONCAT(POW(10,sTempCounts),''),sTempCounts));  
       END IF;  
         
      RETURN sTemp;  
END$$  
  
DELIMITER ;  