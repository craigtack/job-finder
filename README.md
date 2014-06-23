#### Note: Due to Monster.com and Indeed.com's Terms of Use regarding scraping, it is not advisable to use this application.

job-finder
==========

Find job listings more easily with advanced filtering options. Supports Monster and Indeed.
Past listings are saved to prevent duplicate results upon new searches.
Currently a work in progess.

## Background
Currently in search of a job, it got fairly tedious going through listings on websites like Monster and Indeed, getting duplicates or viewing jobs that I already viewed the day before (Note: Indeed allows you to only view new jobs). In lue of that I decided to write a simple CL based app that would not only allow filter options (i.e. don't show me jobs that involve administration) but history check as well, avoiding duplicates.

## Usage
The app "works" to a point at the time of writing. I have yet to test different configurations of city and state searches. With that said, results may vary. Job listings can either be saved to file named "listings.txt" or sent to an email address which requires gmail credentials (Note: If you have two-factor auth enabled, you will need an application specific password). I do plan to have a more complete version uploaded sometime in the near future.

### Configuration
The config.ini should be completed prior to running the application as it contains neccessary information for the app to run successfully. I won't go in to detail about the file here as it's contents are self explanitory.

### Options
Options are keywords based on websites and are defined below.

- monster - search Monster job listings
- indeed - search Indeed job listings
- both - search both Monster and Indeed job listings
- addFilter - add filter. Requires additions parameter, see Parameters

### Parameters
Parameters only apply to the addFilter option. Only one parameter can be applied at a time.
Note about filters: filters are used for negative-matching.
Example: addFilter --title="Administrator"    
Meaning: Don't show jobs containing Administrator (case-insensitive) in the job title.

- --title="title" - specify job title to be filtered
- --company="company" - specify company to filtered
- --location="location" - specify location to be filtered (This currently only supports city name)

## Dependencies
The follwing libraries are required in order to compile and run:
- javax.mail
- ini4j.0.5.2
- jsoup.1.7.3
