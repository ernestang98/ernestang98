from tkinter import Tk, Frame, Label, Text, Scrollbar, Button, Entry
from tkinter.ttk import Combobox
from os import listdir
from Bio import SeqIO
from time import time

from brute_force import bf_search
from rabin_karp import rk_search
from knuth_morris_pratt import kmp_search
from fm_index import fm_search


class Page(Frame):
    def __init__(self, *args, **kwargs):
        Frame.__init__(self, *args, **kwargs)

    def show(self):
        self.lift()


class Settings(Page):
    def __init__(self, *args, **kwargs):
        Page.__init__(self, *args, **kwargs)

        # Choose genome sequence
        genome_label = Label(self, text="Choose Genome Sequence:")
        genome_files = self.get_genome_list()
        self.genome_combobox = Combobox(self, values=genome_files, state="readonly")
        genome_label.grid(row=0, column=0, sticky="W")
        self.genome_combobox.grid(row=0, column=1, sticky="W")

        # Choose algorithim
        algo_label = Label(self, text="Choose Algorithm:")
        self.algo_combobox = Combobox(self, values=["Brute Force", "Rabin-Karp", "Knuth-Morris-Pratt", "FM-Index"],
                                      state="readonly")
        algo_label.grid(row=1, column=0, sticky="W")
        self.algo_combobox.grid(row=1, column=1, sticky="W")

        # Enter query sequence
        query_label = Label(self, text="Enter Query Sequence:")
        self.query_entry = Entry(self)
        query_label.grid(row=2, column=0, sticky="W")
        self.query_entry.grid(row=2, column=1, sticky="W")

        # Continue button
        self.continue_button = Button(self, text="Continue")
        self.continue_button.grid(row=3, sticky="W")

    def get_genome_list(self):
        return listdir("genome_seq")

    def get_genome(self):
        return self.genome_combobox.get()

    def get_algo(self):
        return self.algo_combobox.get()

    def get_query(self):
        return self.query_entry.get()


class Results(Page):
    def __init__(self, *args, **kwargs):
        Page.__init__(self, *args, **kwargs)
        title_label = Label(self, text="Positions Found")

        scrollbar = Scrollbar(self)
        self.results_text = Text(self, state="disabled", wrap="word", yscrollcommand=scrollbar.set)
        scrollbar.configure(command=self.results_text.yview)

        self.back_button = Button(self, text="Back")

        title_label.grid(row=0, column=0)
        self.results_text.grid(row=1, column=0)
        scrollbar.grid(row=1, column=1, sticky="NS")
        self.back_button.grid(row=2, column=0, sticky="W")

    def set_text(self, text):
        self.results_text.configure(state="normal")
        self.results_text.delete("1.0", "end")
        self.results_text.insert("1.0", text)
        self.results_text.configure(state="disabled")

    def compute_positions(self, genome, algo, query):
        genome_record = SeqIO.read("genome_seq/" + genome, "fasta")
        if algo == "Brute Force":
            position_list = bf_search(query, genome_record.seq)
        elif algo == "Rabin-Karp":
            position_list = rk_search(query, genome_record.seq)
        elif algo == "Knuth-Morris-Pratt":
            position_list = kmp_search(query, genome_record.seq)
        elif algo == "FM-Index":
            position_list = fm_search(query, genome_record.seq)

        if position_list:
            self.set_text(", ".join(str(i) for i in position_list))
        else:
            self.set_text("Query sequence not found in genome sequence!")


class Error(Page):
    def __init__(self, *args, **kwargs):
        Page.__init__(self, *args, **kwargs)
        self.label = Label(self, text="")
        self.back_button = Button(self, text="Back")

        self.label.grid(row=0, sticky="W")
        self.back_button.grid(row=1, sticky="SW")

    def set_text(self, text):
        self.label["text"] = text


class MainWindow(Frame):
    def __init__(self, *args, **kwargs):
        Frame.__init__(self, *args, **kwargs)
        self.settings_page = Settings(self)
        self.results_page = Results(self)
        self.error_page = Error(self)

        self.settings_page.continue_button.bind('<Button-1>', self.compute_results)
        self.results_page.back_button.bind('<Button-1>', self.set_settings)
        self.error_page.back_button.bind('<Button-1>', self.set_settings)

        self.settings_page.pack()
        self.results_page.pack_forget()
        self.error_page.pack_forget()

    def set_settings(self, event):
        self.results_page.pack_forget()
        self.error_page.pack_forget()
        self.settings_page.pack()

    def set_results(self, event):
        self.settings_page.pack_forget()
        self.error_page.pack_forget()
        self.results_page.pack()

    def set_error(self, event):
        self.settings_page.pack_forget()
        self.results_page.pack_forget()
        self.error_page.pack()

    def valid_char_check(self, query):
        for char in query:
            if char not in ["A", "C", "G", "T", "U"]:
                return False
        else:
            return True

    def compute_results(self, event):
        genome, algo, query = self.settings_page.get_genome(), self.settings_page.get_algo(), self.settings_page.get_query()
        if not genome:
            self.error_page.set_text("Please select a genome sequence!")
            self.set_error(event)
        elif not algo:
            self.error_page.set_text("Please select a searching algorithm sequence!")
            self.set_error(event)
        elif not query:
            self.error_page.set_text("Please enter a query sequence!")
            self.set_error(event)
        elif not self.valid_char_check(query):
            self.error_page.set_text("Only valid characters are allowed! (A, C, G, T, U)")
            self.set_error(event)
        else:
            try:
                self.results_page.compute_positions(genome, algo, query)
                self.set_results(event)
            except ValueError:
                self.error_page.set_text("Please only use fna files with a single sequence record!")
                self.set_error(event)


if __name__ == '__main__':
    root = Tk()
    windowWidth = root.winfo_reqwidth()
    windowHeight = root.winfo_reqheight()
    positionRight = int(root.winfo_screenwidth() / 2 - windowWidth / 2)
    positionDown = int(root.winfo_screenheight() / 2 - windowHeight / 2)
    root.geometry("+{}+{}".format(positionRight, positionDown))
    root.minsize(300, 100)
    root.title("Algorithms Project 1")
    main = MainWindow(root)
    main.pack()
    root.mainloop()
