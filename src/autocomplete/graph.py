import pandas as pd
import os
import seaborn as sns
import matplotlib.pyplot as plt


def load_data(data, data_path, au='me'):
    files = os.listdir(data_path)
    file_paths = [os.path.join(data_path, f) for f in os.listdir(data_path)]
    for i,f in enumerate(file_paths):
        temp = pd.read_csv(f, names=['n', 'addAll', 'allMatches'])
        temp['Class'] = files[i].split('.')[0]
        temp['Author'] = au
        data = pd.concat([data, temp])
    return data

data = pd.DataFrame()
temp = load_data(data, '/Users/stlp/Dev/CSE373/huskymaps-22wi/src/Sea')
data = pd.concat([data, temp])
# temp = load_data(data, '/Users/stlp/Dev/CSE373/huskymaps-22wi-hhc/src/Sea', au='hhc')
# data = pd.concat([data, temp])

print(data)

def plot_by_class():
    _, ax = plt.subplots(1,2,figsize=(18,5))
    sns.scatterplot(data=data, x='n', y='addAll', hue='Class',ax=ax[0])
    sns.scatterplot(data=data, x='n', y='allMatches', hue='Class', ax=ax[1])
    # sns.lineplot(data=data, x='n', y='addAll', hue='Class',ax=ax[0])
    # sns.lineplot(data=data, x='n', y='allMatches', hue='Class', ax=ax[1])
    ax[0].set_title('addAll')
    ax[1].set_title('allMatches')

def plot_by_author():
    classes = data['Class'].unique()
    _, ax = plt.subplots(2, 2, figsize=(16,7))
    for i,c in enumerate(classes):
        # print(i)
        # print(len(classes) // 2, len(classes) % 2)
        sns.scatterplot(
            data=data[data.Class == c], 
            x='n', y='addAll', 
            hue='Author', 
            ax=ax[i // 2, i % 2])
        sns.scatterplot(
            data=data[data.Class == c], 
            x='n', y='allMatches', 
            hue='Author', 
            ax=ax[i // 2, i % 2])
        ax[i // 2, i % 2].set_title(c)

plot_by_class()
# plot_by_author()
plt.show()
