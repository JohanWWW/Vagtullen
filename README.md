# Vägtullen
Lab 4: Vägtullen

**Made by**\
Mohammed Irheem & Johan Wassberg


## Found Issues
- Last LocalDateTime string is never read from the input file **(Line 14)**
- ``getTotalFeeCost`` returns the maximum fee cost when it should return the minimum fee cost. This can lead to the total fee cost exceeding maximum 60 SEK per day **(Line 37)**
- In method ``getTotalFeeCost`` the maximum value of two toll fees are always added to variable ``totalFee`` which is incorrect **(Line 34)**
- Logic error at **Line 50**. This line ignores time values where the minute component is less than 30 minutes. Leads to incorrect calculations
- ``getTotalFeeCost`` never checks if minimum date and maximum date read from the file are two different days. This leads to the total fee cost being too low (maximum 60 SEK)
- If the date and time strings in the input file are not sorted chronologically, this leads to a logic error at **Line 30**, ``diffInMinutes`` at **Line 29** becomes negative
- The constructor does not catch ``NoSuchElementException`` which is thrown if the input file is empty **(Line 13)**
- The constructor does not catch ``DateTimeParseException`` which is thrown if a date time string from the input file is not "yyyy-MM-dd HH:mm"-formatted **(Line 16)**
- The constructor does not catch ``NullPointerException`` which is thrown if constructor parameter ``inputFile`` is null.
- The file stream is never closed which can lead to ``InvalidStateException`` **(Line 14)**