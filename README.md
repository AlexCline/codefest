codefest
========

Sample SQL query to get pickup day from pickups table
    SELECT * FROM pickup WHERE street_name_base = "NEGLEY" AND zip = 15217 AND ((left_low < 1328 AND left_high > 1328) OR (right_low < 1328 AND right_high > 1328));

SQL Commands to cleanup pickups data
    UPDATE pickup SET division = "CENTRAL" WHERE division = "Central";
    UPDATE pickup SET division = "EASTERN" WHERE division = "East";
