#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#define BUFFERSIZE 4096
void displayPrompt();
void inputOutputRedirection(char** argumentList);
int main(int argc, char* argv[]) {
    char cmd[BUFFERSIZE];
    char exitString[BUFFERSIZE] = "exit\0";
    char* argumentList[BUFFERSIZE];
    char *token;
    pid_t pid;
    int status;
    int n;
    chdir(getenv("HOME"));
    while(1) {
        displayPrompt();
        n= read(STDIN_FILENO, cmd, BUFFERSIZE);
        if( n > 1) { // if there is no command it will do nothing
            cmd[n-1] = '\0';
            // use strtok and a while loop to tokenize the user input.
            token = strtok(cmd, " ");
            int i = 0;
            while( token != NULL) {
                argumentList[i] = token;
                i++;
                token = strtok(NULL, " ");
            } // while
            argumentList[i++] = NULL;
            // checks if the user puts in exit.
            if((strcmp(cmd, exitString)) == 0) {
                exit(0);
            } else if((strcmp(argumentList[0], "cd")) == 0){ // cd case
                 // if it is just cd or cd ~ it will go to home
                if(i == 2 || strcmp(argumentList[1], "~") == 0 ) {
                    argumentList[1] = getenv("HOME");
                } // if
                if(chdir(argumentList[1]) == -1) {
                    perror("chdir");
                } // if
            }else {
                if((pid = fork()) < 0) {
                    perror("FORK ERROR");
                } else if(pid == 0) { // child process
                    inputOutputRedirection(argumentList);
                    // uses execvp to do the process the user put in.
                    if(execvp(argumentList[0], argumentList) == -1) {
                        perror("execvp");
                        return EXIT_FAILURE;
                    } // if
                } else { // parent process
                    wait(&status);
                } // if
            } // if
        } // if
    } // while
} // main

/**
 * checks if there is input or output redirection and then responds accordingly.
 *
 * @param argumentList- a array of strings that holds the arguments already parsed.
 */
void inputOutputRedirection(char** argumentList) {
    char* copy;
    char* returnList[BUFFERSIZE];
    char* file;
    int size = 0;
    int fd;
    // puts the commands into a different array called return list.
    for(int i = 0; argumentList[i] != NULL; i++) {
        copy = argumentList[i];
        size++;
        if(strcmp(copy, "<") == 0 || strcmp(copy, ">") == 0 || strcmp(copy, ">>") == 0 ||
        strcmp(copy, "<<") == 0) {
            returnList[i] = NULL;
            break;
        } // if
        returnList[i] = argumentList[i];
    } // for
    // checks for > < or >>
    for(int i = 0; argumentList[i] != NULL; i++) {
        copy = argumentList[i];
        if(strcmp(copy, ">") == 0) { // output redirection
            file = argumentList[i + 1];
            if((fd = open(file, O_WRONLY | O_TRUNC | O_CREAT, 0644)) < 0) {
                perror("open");
            } // if
            dup2(fd, STDOUT_FILENO);
            close(fd);
        } else if(strcmp(copy, "<") == 0) { // input redirection
            int fd;
            file = argumentList[i + 1];
            if((fd = open(file, O_RDWR)) < 0) {
                perror("open");
            } // if
            dup2(fd, STDIN_FILENO);
            close(fd);
        } else if(strcmp(copy, ">>") == 0) { //output append mode
            file = argumentList[i + 1];
            if((fd = open(file, O_WRONLY | O_APPEND | O_CREAT, 0644)) < 0) {
                perror("open");
            } // if
            dup2(fd, STDOUT_FILENO);
            close(fd);
        } // if
    } // for
    // puts the commands into argumentList.
    for(int i = 0; i < size; i++) {
        argumentList[i] = returnList[i];
    } // for
} // inputOutputRedirection

/**
 * displays the prompt to the user.
 */
void displayPrompt() {
    char buffer[300];
    char* home;
    char compare[300];
    char display[300];
    write(STDOUT_FILENO, "1730sh:", 6);
    // gets directory
    if(getcwd(buffer, sizeof(buffer)) == NULL) {
        perror("cwd");
    } // if
    // gets home
    if((home = getenv("HOME")) == NULL) {
        perror("env");
    } // if
    // compares and removes the home and replaces it with ~ if home
    // is there.
    memcpy(compare, buffer, strlen(home));
    if(strcmp(compare, home) == 0) {
        write(STDOUT_FILENO, "~", 1);
        for(int i = 0; buffer[i] != '\0'; i++) {
            display[i] = buffer[(i +strlen(home))];
        } // for
    } // if
    write(STDOUT_FILENO, display, strlen(display));
    write(STDOUT_FILENO, "$ ", 2);
} // displayPrompt
