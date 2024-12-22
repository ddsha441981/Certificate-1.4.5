export const getUniqueColumns = (data) => {
    const uniqueColumns = new Set();
    data.forEach(item => {
      Object.keys(item).forEach(key => {
        uniqueColumns.add(key);
      });
    });
    return Array.from(uniqueColumns);
  }
  