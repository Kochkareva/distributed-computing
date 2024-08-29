import numpy as np
import concurrent.futures
import time


def multiplication_rows(row, matrix_b):
    return np.dot(row, matrix_b)


def parallel_matrix_multiplication(matrix_a, matrix_b, num_threads):
    num_rows_a, num_cols_a = matrix_a.shape
    num_rows_b, num_cols_b = matrix_b.shape
    assert num_cols_a == num_rows_b, "Размеры матриц несовместимы"

    with concurrent.futures.ThreadPoolExecutor(max_workers=num_threads) as executor:
        start_time = time.time()
        results = []
        for i in range(num_rows_a):
            result = executor.submit(multiplication_rows, matrix_a[i], matrix_b)
            results.append((i, result))
        sorted_results = sorted(results, key=lambda x: x[0])
        result_matrix = np.vstack(
            [result.result() for _, result in sorted_results])
        end_time = time.time()

    execution_time = end_time - start_time
    return result_matrix, execution_time


def test(parallel):
    a = np.array([[12, 42, 64],
                  [38, 4, 21]])
    b = np.array([[35, 2],
                  [64, 41],
                  [5, 33]])
    if parallel:
        result = parallel_matrix_multiplication(a, b, num_threads=2)
    else:
        result = parallel_matrix_multiplication(a, b, num_threads=1)
    print("Результат умножения:")
    print(result[0])
    print("Время выполнения: " + str(result[1]) + " с")


def matrix100x100(parallel):
    a = np.random.randint(0, 100, size=(100, 100))
    b = np.random.randint(0, 100, size=(100, 100))
    if parallel:
        result = parallel_matrix_multiplication(a, b, num_threads=100)
    else:
        result = parallel_matrix_multiplication(a, b, num_threads=1)
    print("Результат умножения:")
    print(result[0])
    print("Время выполнения: " + str(result[1]) + " с")


def matrix300x300(parallel):
    a = np.random.randint(0, 100, size=(300, 300))
    b = np.random.randint(0, 100, size=(300, 300))
    if parallel:
        result = parallel_matrix_multiplication(a, b, num_threads=300)
    else:
        result = parallel_matrix_multiplication(a, b, num_threads=1)
    print("Результат умножения:")
    print(result[0])
    print("Время выполнения: " + str(result[1]) + " с")


def matrix500x500(parallel):
    a = np.random.randint(0, 100, size=(500, 500))
    b = np.random.randint(0, 100, size=(500, 500))
    if parallel:
        result = parallel_matrix_multiplication(a, b, num_threads=500)
    else:
        result = parallel_matrix_multiplication(a, b, num_threads=1)
    print("Результат умножения:")
    print(result[0])
    print("Время выполнения: " + str(result[1]) + " с")


if __name__ == '__main__':
    # test(parallel=True)
    # print("Матрица 100x100:")
    # print("Результат умножения параллельно:")
    # matrix100x100(parallel=True)
    # print("Результат умножения обычным способом:")
    # matrix100x100(parallel=False)
    # print("Матрица 300x300:")
    # print("Результат умножения параллельно:")
    # matrix300x300(parallel=True)
    # print("Результат умножения обычным способом:")
    # matrix300x300(parallel=False)
    print("Матрица 500x500:")
    print("Результат умножения параллельно:")
    matrix500x500(parallel=True)
    print("Результат умножения обычным способом:")
    matrix500x500(parallel=False)

