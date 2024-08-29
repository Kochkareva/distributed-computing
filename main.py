import time
import numpy as np
from multiprocessing import Pool


def calculate_determinant(args):
    matrix, i = args
    multiplier = matrix[0][i]
    if i % 2 != 0:
        multiplier *= -1
    matrix = np.delete(matrix, 0, axis=0)
    submatrix = np.delete(matrix, i, axis=1)
    return np.linalg.det(submatrix) * multiplier


def parallel_determinant(matrix, parallel):
    n = matrix.shape[0]
    if parallel:
        pool = Pool(processes=n)
        results = []
        start_time = time.time()
        for i in range(n):
            results.append(pool.apply_async(calculate_determinant, args=((matrix, i),)))
        pool.close()
        pool.join()
        result = np.sum([res.get() for res in results])
        end_time = time.time()
    else:
        start_time = time.time()
        result = np.linalg.det(matrix)
        end_time = time.time()
    execution_time = end_time - start_time
    return result, execution_time


def test(parallel):
    mx = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
    result = parallel_determinant(mx, parallel)
    print(f"Определитель матрицы: {result[0]}")
    print(f"Время выполнения: {result[1]} сек.")


def matrix10x10(parallel):
    mx = np.random.randint(0, 100, size=(10, 10))
    result = parallel_determinant(mx, parallel)
    print(f"Определитель матрицы: {result[0]}")
    print(f"Время выполнения: {result[1]} сек.")


def matrix25x25(parallel):
    mx = np.random.randint(0, 100, size=(25, 25))
    result = parallel_determinant(mx, parallel)
    print(f"Определитель матрицы: {result[0]}")
    print(f"Время выполнения: {result[1]} сек.")


def matrix50x50(parallel):
    mx = np.random.randint(0, 100, size=(50, 50))
    result = parallel_determinant(mx, parallel)
    print(f"Определитель матрицы: {result[0]}")
    print(f"Время выполнения: {result[1]} сек.")


if __name__ == '__main__':
    # print("Матрица 10x10:")
    # print("Результат нахождения детерминанта параллельно:")
    # matrix10x10(parallel=True)
    # print("Результат нахождения детерминанта обычным способом:")
    # matrix10x10(parallel=False)
    # print("Матрица 25x25:")
    # print("Результат нахождения детерминанта параллельно:")
    # matrix25x25(parallel=True)
    # print("Результат нахождения детерминанта обычным способом:")
    # matrix25x25(parallel=False)
    print("Матрица 50x50:")
    print("Результат нахождения детерминанта параллельно:")
    matrix50x50(parallel=True)
    print("Результат нахождения детерминанта обычным способом:")
    matrix50x50(parallel=False)
